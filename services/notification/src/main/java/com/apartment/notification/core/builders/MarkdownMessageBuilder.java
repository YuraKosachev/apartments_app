package com.apartment.notification.core.builders;

import com.apartment.kafka.models.Message;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.core.io.ClassPathResource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class MarkdownMessageBuilder {

    public static String buildFromTemplate(String taskName, Message model) throws Exception {
        // 1. Загружаем шаблон из classpath
        var resource = new ClassPathResource("templates/task_message.md");
        String template = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // 2. Подготавливаем переменные
//        String pricesBlock = Arrays.stream(model.getPrices())
//                .collect(Collectors.joining("\n"));

        String pricesBlock = joinCollection(model.getPrices(),"\n\u00A0\u00A0\u00A0\u00A0");
        String messageBlock = joinCollection(model.getMessage(), "\n");
        String descriptionBlock = joinCollection(model.getDescription(), "\n\u00A0\u00A0\u00A0\u00A0");

        Map<String, Object> values = new HashMap<>();
        values.put("task", escapeMarkdown(taskName));
        values.put("address", escapeMarkdown(messageBlock));
        values.put("url", escapeMarkdown(model.getUrl()));
        values.put("specifications", escapeMarkdown(descriptionBlock));
        values.put("imageUrl", escapeMarkdown(model.getPhoto()));
        values.put("prices_block", escapeMarkdown(pricesBlock));

        // 3. Подставляем значения
        return new StringSubstitutor(values).replace(template);
    }

    public static String joinCollection(String[] source, String delimiter) {
        return Arrays.stream(source)
                .collect(Collectors.joining(delimiter));
    }
    // 🧹 Экранируем MarkdownV2 спецсимволы (для Telegram)
    private static String escapeMarkdown(String input) {
        if (input == null) return "";
        return input.replaceAll("([_*\\[\\]()~`>#+\\-=|{}.!])", "\\\\$1");
    }
}

