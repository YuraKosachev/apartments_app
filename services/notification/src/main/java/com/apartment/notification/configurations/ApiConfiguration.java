package com.apartment.notification.configurations;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiConfiguration {

    @Value("${api.telegram.message}")
    private String teleframMessageApiFormat;

    @Value("${api.telegram.botkey}")
    private String teleframBotKey;

    public String getTeleframMessageApiFormat() {
        return teleframMessageApiFormat.formatted(teleframBotKey);
    }

}