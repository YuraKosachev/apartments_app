package com.apartment.notification.core.builders;

import com.apartment.kafka.models.Message;
import org.apache.commons.text.StringSubstitutor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class MarkdownMessageBuilder {

    public static String buildFromTemplate(String taskName, Message model) throws Exception {
        // 1. Загружаем шаблон
        String template = Files.readString(Path.of("src/main/resources/templates/task_message.md"));

        // 2. Подготавливаем переменные
        String pricesBlock = Arrays.stream(model.getPrices())
                .collect(Collectors.joining("\n"));

        Map<String, Object> values = new HashMap<>();
        values.put("task", escapeMarkdown(taskName));
        values.put("address", escapeMarkdown(model.getMessage()));
        values.put("url", escapeMarkdown(model.getUrl()));
        values.put("specifications", escapeMarkdown(model.getDescription()));
        values.put("imageUrl", escapeMarkdown(model.getPhoto()));
        values.put("prices_block", escapeMarkdown(pricesBlock));

        // 3. Подставляем значения
        return new StringSubstitutor(values).replace(template);
    }

    // 🧹 Экранируем MarkdownV2 спецсимволы (важно для Telegram)
    private static String escapeMarkdown(String input) {
        if (input == null) return "";
        return input.replaceAll("([_*\\[\\]()~`>#+\\-=|{}.!])", "\\\\$1");
    }
}
