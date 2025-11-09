package com.apartment.apartment_api.tasks;

import com.apartment.apartment_api.core.builders.AdvancedSpecificationBuilder;
import com.apartment.apartment_api.core.kafka.Consumer;
import com.apartment.apartment_api.core.kafka.Producer;
import com.apartment.apartment_api.core.mappers.ApartmentMapper;
import com.apartment.apartment_api.core.models.dtos.FoundTask;
import com.apartment.apartment_api.core.models.entities.Apartment;
import com.apartment.apartment_api.core.models.entities.Task;
import com.apartment.apartment_api.core.models.entities.TaskApartment;
import com.apartment.apartment_api.core.models.entities.compositkeys.TaskApartmentKey;
import com.apartment.apartment_api.repositories.ApartmentRepository;
import com.apartment.apartment_api.repositories.TaskApartmentRepository;
import com.apartment.apartment_api.repositories.TaskRepository;
import com.apartment.kafka.enums.NotificationType;
import com.apartment.kafka.models.Message;
import com.apartment.kafka.models.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationTask {

    private final TaskRepository taskRepository;
    private final ApartmentRepository apartmentRepository;
    private final ApartmentMapper apartmentMapper;
    private final TaskApartmentRepository taskApartmentRepository;
    private final AdvancedSpecificationBuilder<Apartment> specificationBuilder;
    private final Producer producer;

    @Scheduled(cron = "${task.notification.cron}")
    @Transactional
    public void handling() {
        log.info("Handling tasks");
        var tasks = taskRepository.findAll();
        var foundIds = tasks.stream()
                .filter(t -> t.getFound() != null)
                .flatMap(t -> t.getFound().stream())
                .map(f -> new FoundTask(f.getTask().getId(), f.getApartment().getId()))
                .collect(Collectors.toSet());
        for (var task : tasks) {
            try {
                var specification = specificationBuilder.build(task.getPredicate(), Apartment.class);
                var apartments = apartmentRepository.findAll(specification).stream()
                        .filter(ap -> !foundIds.contains(new FoundTask(task.getId(), ap.getId())))
                        .map(apartmentMapper::toApartmentShortDto).collect(Collectors.toList());
                if (apartments.isEmpty()) {
                    log.info("Handling task completed with no apartment found");
                    continue;
                }
                var messages = apartments.stream().map(apartmentMapper::toMessage).toArray(Message[]::new);
                var notificationMessage = NotificationMessage.builder()
                        .type(NotificationType.TASK_INFO)
                        .messages(messages)
                        .topic(task.getName())
                        .to(new String[]{task.getIssuerId().toString()})
                        .build();

                producer.sendNotification(notificationMessage);

                var taskApartments = apartments.stream().map(ap -> TaskApartment.builder()
                        .id(TaskApartmentKey.builder().apartmentId(ap.getApartmentId()).taskId(task.getId()).build())
                        .apartment(Apartment.builder().id(ap.getApartmentId()).build())
                        .task(task)
                        .build()).toList();

                taskApartmentRepository.saveAll(taskApartments);

                log.info("Handling task completed");
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
