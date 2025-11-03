package com.apartment.data_provider.core.kafka;

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

import java.util.Arrays;


@Service
@RequiredArgsConstructor
@Slf4j
public class Producer {
    private final KafkaTemplate<String, NotificationMessage> notificationTemplate;
    private final KafkaTemplate<String, ApartmentMessage> apartmentTemplate;
    private final int limit = 1000;

    public void sendNotification(NotificationMessage message) {

        var messages = message.getMessages();

        for (var i = 0; true; i += limit) {
            var messagesToSend = Arrays.stream(messages).skip(i)
                    .limit(limit)
                    .toArray(com.apartment.kafka.models.Message[]::new);
            if (messagesToSend.length <= 0) {
                break;
            }
            message.setMessages(messagesToSend);
            notificationTemplate.send(getMessageBuilder(message, KafkaConstants.NOTIFICATION_TOPIC));
        }
    }

    public void sendApartment(ApartmentMessage message) {

        var apartments = message.getApartments();
        for (var i = 0; true; i += limit) {
            var apartmentsToSend = Arrays.stream(apartments).skip(i)
                    .limit(limit)
                    .toArray(com.apartment.kafka.models.Apartment[]::new);
            if (apartmentsToSend.length <= 0) {
                break;
            }
            message.setApartments(apartmentsToSend);
            apartmentTemplate.send(getMessageBuilder(message, KafkaConstants.APARTMENT_TOPIC));
        }
    }

    private <T> Message<T> getMessageBuilder(T message, String topic) {
        return MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();
    }
}
