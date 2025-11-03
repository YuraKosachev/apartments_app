package com.apartment.notification.core.models.dtos;

import com.apartment.notification.core.enums.ParseMode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TelegramMessageRequest {
    String message;
    ParseMode parseMode;
    String chatId;
}
