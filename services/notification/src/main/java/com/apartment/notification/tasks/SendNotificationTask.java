package com.apartment.notification.tasks;

import com.apartment.kafka.enums.NotificationType;
import com.apartment.notification.core.enums.NotificationStatus;
import com.apartment.notification.core.enums.ParseMode;
import com.apartment.notification.core.interfaces.apiclients.TelegramApiMessage;
import com.apartment.notification.core.models.dtos.TelegramMessageRequest;
import com.apartment.notification.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendNotificationTask {

    private final NotificationRepository notificationRepository;
    private final TelegramApiMessage telegramApiMessage;

    @Scheduled(cron = "${task.send.notification_cron}")
    public void sendNotification() {
        var notifications = notificationRepository
                .getAllByTypes(Set.of(NotificationStatus.NEW, NotificationStatus.ERROR), Set.of(NotificationType.TASK_INFO));

        if(notifications.isEmpty()) {
            return;
        }
        notifications.forEach(notification -> notification.setStatus(NotificationStatus.HANDLING));
        notificationRepository.saveAll(notifications);

        for (var notification : notifications) {
            try {
                if (notification.getTo() == null || notification.getTo().length <= 0)
                    continue;

                for (var to : notification.getTo()) {
                    var request = TelegramMessageRequest.builder()
                            .chatId(to)
                            .message(notification.getBody())
                            .parseMode(ParseMode.MARKDOWNV2)
                            .build();
                    telegramApiMessage.sendMessage(request);
                }
                notification.setStatus(NotificationStatus.SENT);
            } catch (Exception e) {
                notification.setStatus(NotificationStatus.ERROR);
            }
        }
        notificationRepository.saveAll(notifications);
    }
}

