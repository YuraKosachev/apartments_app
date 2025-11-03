package com.apartment.data_provider.tasks;

import com.apartment.data_provider.core.enums.EventType;
import com.apartment.data_provider.core.enums.GeoDataStatus;
import com.apartment.data_provider.core.kafka.Producer;
import com.apartment.data_provider.core.mappers.ApartmentMapper;
import com.apartment.data_provider.core.mappers.NotificationMapper;
import com.apartment.data_provider.repositories.ApartmentRepository;
import com.apartment.data_provider.repositories.EventLogRepository;
import com.apartment.kafka.enums.Event;
import com.apartment.kafka.enums.NotificationType;
import com.apartment.kafka.models.Apartment;
import com.apartment.kafka.models.ApartmentMessage;
import com.apartment.kafka.models.Message;
import com.apartment.kafka.models.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendDataTask {

    private final EventLogRepository eventLogRepository;
    private final ApartmentRepository apartmentRepository;
    private final NotificationMapper notificaitonMapper;
    private final ApartmentMapper apartmentMapper;
    private final Producer producer;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Scheduled(cron = "${task.onliner.notification_cron}")
    public void sendNotification() {

        log.info("[%s]Sending notification".formatted(LocalDateTime.now().format(formatter)));
        try {

            var logs = eventLogRepository.getAllNotSendByType(Set.of(EventType.ERROR));
            var logMessages = logs.stream()
                    .map(notificaitonMapper::toNotificationMessage)
                    .toArray(Message[]::new);

            var message = NotificationMessage
                    .builder()
                    .messages(logMessages)
                    .type(NotificationType.SERVICE_ERROR)
                    .topic("Data provider notification error")
                    .build();

            producer.sendNotification(message);

            logs.forEach(m->m.setSentAt(LocalDateTime.now()));
            eventLogRepository.saveAll(logs);
            log.info("[%s]Notification sent successfully".formatted(LocalDateTime.now().format(formatter)));
        }catch (Exception e) {
            log.error("[%s]Notification sent error %s".formatted(LocalDateTime.now().format(formatter), e.getMessage()));
        }
    }


    @Scheduled(cron = "${task.onliner.apartment_cron}")
    public void sendApartment() {

        log.info("[%s]Sending apartments".formatted(LocalDateTime.now().format(formatter)));
        try {
            var apartments = apartmentRepository.getAllNotInEvents(Set.of(Event.SENT), Set.of(GeoDataStatus.PROCESSED));

            if(apartments.isEmpty()) {
                log.info("[%s]Apartments sent successfully".formatted(LocalDateTime.now().format(formatter)));
                return;
            }

            var apartmentMessages = apartments.stream()
                    .map(apartmentMapper::getApartmentMessage)
                    .toArray(Apartment[]::new);

            var apartmentMessage = ApartmentMessage.builder()
                    .apartments(apartmentMessages)
                    .sentAt(LocalDateTime.now())
                    .build();

            producer.sendApartment(apartmentMessage);

            apartments.forEach(apartment -> {
                apartment.setEvent(Event.SENT);
                apartment.setLastSentAt(LocalDateTime.now());
            });
            apartmentRepository.saveAll(apartments);
            log.info("[%s]Apartments sent successfully".formatted(LocalDateTime.now().format(formatter)));
        }catch (Exception e) {
            log.error("[%s]Apartments sent error %s".formatted(LocalDateTime.now().format(formatter), e.getMessage()));
        }
    }

}
