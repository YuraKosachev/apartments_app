package com.apartment.apartment_api.core.builders;

import com.apartment.apartment_api.core.exceptions.SpecificationException;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Универсальный парсер строковых условий в JPA Specification<T>.
 * Поддерживает операции: =, !=, >, <, >=, <=, LIKE, CONTAINS, STARTS WITH, ENDS WITH, IS NULL, IS NOT NULL, AND, OR, ()
 */
@Component
public class AdvancedSpecificationBuilder<T> {

    private static final Pattern CONDITION_PATTERN = Pattern.compile(
            "(\\w+(?:\\.\\w+)*)\\s*(=|!=|>=|<=|>|<|LIKE|CONTAINS|STARTS WITH|ENDS WITH|IS NULL|IS NOT NULL)\\s*'?(.*?)'?$",
            Pattern.CASE_INSENSITIVE);

    /**
     * Построение Specification на основе фильтра и класса сущности
     */
    public Specification<T> build(String filter, Class<T> entityClass) {
        validate(filter, entityClass);
        return buildSpecification(filter);
    }

    /**
     * Валидация фильтра
     */
    public void validate(String filter, Class<T> entityClass) {
        if (filter == null || filter.isBlank()) {
            throw new SpecificationException("Фильтр не может быть пустым");
        }
        validateSyntax(filter);
        validateFields(entityClass, filter);
    }

    private void validateSyntax(String filter) {
        int balance = 0;
        for (char c : filter.toCharArray()) {
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) throw new SpecificationException("Ошибка в расстановке скобок");
        }
        if (balance != 0) throw new SpecificationException("Непарные скобки в фильтре");

        if (!filter.matches("(?i)[\\p{L}\\p{N}\\s\\(\\)><=!'.&|,]+")) {
            throw new SpecificationException("Фильтр содержит недопустимые символы");
        }


//        if (!filter.matches("(?i)[\\w\\s\\(\\)><=!'.&|\\.]+")) {
//            throw new SpecificationException("Фильтр содержит недопустимые символы");
//        }
    }

    private void validateFields(Class<T> entityClass, String filter) {
        Set<String> entityFields = Arrays.stream(entityClass.getDeclaredFields())
                .map(f -> f.getName().toLowerCase())
                .collect(Collectors.toSet());

        Matcher matcher = CONDITION_PATTERN.matcher(filter);
        while (matcher.find()) {
            String fieldPath = matcher.group(1).toLowerCase();
            String rootField = fieldPath.contains(".") ? fieldPath.split("\\.")[0] : fieldPath;
            if (!entityFields.contains(rootField)) {
                throw new SpecificationException(
                        "Поле '" + rootField + "' отсутствует в " + entityClass.getSimpleName());
            }
        }
    }

    private Specification<T> buildSpecification(String filter) {
        return parseExpression(filter);
    }

    private Specification<T> parseExpression(String expression) {
        expression = expression.trim();

        if (expression.startsWith("(") && expression.endsWith(")") && isBalanced(expression.substring(1, expression.length() - 1))) {
            return parseExpression(expression.substring(1, expression.length() - 1));
        }

        int orIndex = findOperatorIndex(expression, "OR");
        if (orIndex != -1) {
            String left = expression.substring(0, orIndex);
            String right = expression.substring(orIndex + 2);
            Specification<T> leftSpec = parseExpression(left);
            Specification<T> rightSpec = parseExpression(right);
            return (root, query, cb) ->
                    cb.or(leftSpec.toPredicate(root, query, cb), rightSpec.toPredicate(root, query, cb));
        }

        int andIndex = findOperatorIndex(expression, "AND");
        if (andIndex != -1) {
            String left = expression.substring(0, andIndex);
            String right = expression.substring(andIndex + 3);
            Specification<T> leftSpec = parseExpression(left);
            Specification<T> rightSpec = parseExpression(right);
            return (root, query, cb) ->
                    cb.and(leftSpec.toPredicate(root, query, cb), rightSpec.toPredicate(root, query, cb));
        }

        return parseCondition(expression);
    }

    private boolean isBalanced(String str) {
        int balance = 0;
        for (char c : str.toCharArray()) {
            if (c == '(') balance++;
            if (c == ')') balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }

    private int findOperatorIndex(String expression, String operator) {
        int depth = 0;
        for (int i = 0; i < expression.length() - operator.length() + 1; i++) {
            char c = expression.charAt(i);
            if (c == '(') depth++;
            if (c == ')') depth--;
            if (depth == 0 && expression.regionMatches(true, i, operator, 0, operator.length())) {
                return i;
            }
        }
        return -1;
    }

    private Specification<T> parseCondition(String condition) {
        condition = condition.trim();

        // IS NULL / IS NOT NULL
        if (condition.matches("(?i)\\w+(\\.\\w+)*\\s+IS\\s+(NOT\\s+)?NULL")) {
            String[] parts = condition.split("\\s+");
            String fieldPath = parts[0];
            boolean not = condition.toUpperCase().contains("NOT");
            return (root, query, cb) -> not ? cb.isNotNull(getPath(root, fieldPath)) : cb.isNull(getPath(root, fieldPath));
        }

        // Проверка на IN
        Matcher inMatcher = Pattern.compile("(?i)(\\w+(?:\\.\\w+)*)\\s+IN\\s*\\((.*?)\\)").matcher(condition);
        if (inMatcher.find()) {
            String fieldPath = inMatcher.group(1);
            String valueList = inMatcher.group(2); // "2,3,5"
            return (root, query, cb) -> {
                Path<?> path = getPath(root, fieldPath);
                Class<?> type = path.getJavaType();
                List<Object> castedItems = Arrays.stream(valueList.split(","))
                        .map(String::trim)
                        .map(v -> castValue(path, v))
                        .collect(Collectors.toList());
                return path.in(castedItems);
            };
        }

        // Общие операторы (=, !=, >, <, >=, <=, LIKE, CONTAINS, STARTS WITH, ENDS WITH)
        Matcher matcher = CONDITION_PATTERN.matcher(condition);
        if (!matcher.find()) {
            throw new SpecificationException("Неверное условие фильтра: " + condition);
        }

        String fieldPath = matcher.group(1);
        String operator = matcher.group(2).toUpperCase(Locale.ROOT);
        String value = matcher.group(3);

        return (root, query, cb) -> {
            Path<?> path = getPath(root, fieldPath);
            Object typedValue = castValue(path, value);

            return switch (operator) {
                case "=" -> cb.equal(path, typedValue);
                case "!=" -> cb.notEqual(path, typedValue);
                case ">" -> cb.greaterThan((Path<Comparable>) path, (Comparable) typedValue);
                case "<" -> cb.lessThan((Path<Comparable>) path, (Comparable) typedValue);
                case ">=" -> cb.greaterThanOrEqualTo((Path<Comparable>) path, (Comparable) typedValue);
                case "<=" -> cb.lessThanOrEqualTo((Path<Comparable>) path, (Comparable) typedValue);
                case "LIKE" -> cb.like(cb.lower((Path<String>) path), value.toLowerCase());
                case "CONTAINS" -> cb.like(cb.lower((Path<String>) path), "%" + value.toLowerCase() + "%");
                case "STARTS WITH" -> cb.like(cb.lower((Path<String>) path), value.toLowerCase() + "%");
                case "ENDS WITH" -> cb.like(cb.lower((Path<String>) path), "%" + value.toLowerCase());
                default -> throw new SpecificationException("Неизвестный оператор: " + operator);
            };
        };
    }

    private Path<?> getPath(Root<T> root, String fieldPath) {
        String[] parts = fieldPath.split("\\.");
        From<?, ?> from = root;
        Path<?> path = null;

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            Class<?> javaType;

            // Определяем тип текущего поля
            if (i == 0) {
                javaType = root.get(part).getJavaType();
            } else {
                javaType = path.getJavaType();
            }

            if (Collection.class.isAssignableFrom(javaType)) {
                // JOIN для коллекции (OneToMany)
                from = from.join(part, JoinType.LEFT);
                path = from;
            } else if (i < parts.length - 1) {
                // Вложенная сущность (ManyToOne)
                from = from.join(part, JoinType.LEFT);
                path = from;
            } else {
                // Последнее поле — используем get
                path = from.get(part);
            }
        }

        return path;
    }


    /**
     * Кастинг значения для Path
     */
    private Object castValue(Path<?> path, String value) {
        Class<?> type = path.getJavaType();
        if (type.equals(Integer.class) || type.equals(int.class)) return Integer.valueOf(value);
        if (type.equals(Long.class) || type.equals(long.class)) return Long.valueOf(value);
        if (type.equals(Double.class) || type.equals(double.class)) return Double.valueOf(value);
        if (type.equals(Boolean.class) || type.equals(boolean.class)) return Boolean.valueOf(value);

        // Обработка Enum
        if (type.isEnum()) {
            try {
                @SuppressWarnings({ "unchecked", "rawtypes" })
                Object enumValue = Enum.valueOf((Class<Enum>) type, value.toUpperCase(Locale.ROOT));
                return enumValue;
            } catch (IllegalArgumentException e) {
                throw new SpecificationException(
                        "Неверное значение для enum " + type.getSimpleName() + ": " + value
                );
            }
        }

        return value;
    }
}

