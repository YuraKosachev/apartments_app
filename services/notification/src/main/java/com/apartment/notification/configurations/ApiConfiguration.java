package com.apartment.notification.configurations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfiguration {

    @Value("${api.telegram.message}")
    private String telegramMessageApiFormat;

    @Value("${api.telegram.image}")
    private String telegramImageApiFormat;

    @Value("${api.telegram.botkey}")
    private String telegramBotKey;

    public String getTelegramMessageApiFormat() {
        return telegramMessageApiFormat.formatted(telegramBotKey);
    }

    public String getTelegramImageApiFormat() {
        return telegramImageApiFormat.formatted(telegramBotKey);
    }

}