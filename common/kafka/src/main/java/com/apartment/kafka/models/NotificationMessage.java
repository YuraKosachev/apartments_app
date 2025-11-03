package com.apartment.kafka.models;


import com.apartment.kafka.enums.NotificationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationMessage{
    NotificationType type;
    String topic;
    String[] to;
    Message[] messages;
}
