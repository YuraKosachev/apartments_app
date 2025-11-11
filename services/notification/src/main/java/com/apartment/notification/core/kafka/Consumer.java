package com.apartment.notification.core.kafka;

import com.apartment.kafka.constants.KafkaConstants;
import com.apartment.kafka.enums.Event;
import com.apartment.kafka.enums.NotificationType;
import com.apartment.kafka.models.ApartmentMessage;
import com.apartment.kafka.models.NotificationMessage;
import com.apartment.notification.core.builders.MarkdownMessageBuilder;
import com.apartment.notification.core.models.entities.Notification;
import com.apartment.notification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {

    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = KafkaConstants.NOTIFICATION_TOPIC)
    public void consume(NotificationMessage message) {
        try {
            log.info("Consuming message...");

            for (var item : message.getMessages()) {
                var body = message.getType() != NotificationType.TASK_INFO
                        ? MarkdownMessageBuilder.joinCollection(item.getMessage(), "\n")
                        : MarkdownMessageBuilder.buildFromTemplate(message.getTopic(), item);
                var notification = Notification
                        .builder()
                        .type(message.getType())
                        .photo(item.getPhoto())
                        .body(body)
                        .to(message.getTo())
                        .subject(message.getTopic())
                        .build();
                notificationRepository.save(notification);
            }

            log.info("Consuming message succeeded...");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}