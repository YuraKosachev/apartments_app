package com.apartment.apartment_api.core.kafka;

import com.apartment.kafka.constants.KafkaConstants;
import com.apartment.kafka.models.ApartmentMessage;
import com.apartment.kafka.models.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Producer {
    private final KafkaTemplate<String, NotificationMessage> notificationTemplate;

    public void sendNotification(NotificationMessage message) {
        notificationTemplate.send(getMessageBuilder(message, KafkaConstants.NOTIFICATION_TOPIC));
    }

    private <T> Message<T> getMessageBuilder(T message, String topic) {
        return MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }
}
