package com.apartment.data_provider.core.mappers;

import com.apartment.data_provider.core.models.entities.EventLog;
import com.apartment.kafka.models.Message;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public Message toNotificationMessage(EventLog eventLog) {
        if(eventLog == null) return null;
        return Message.builder()
                .message(eventLog.getContent())
                .build();
    }
}
