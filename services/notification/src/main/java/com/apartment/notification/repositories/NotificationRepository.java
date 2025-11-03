package com.apartment.notification.repositories;

import com.apartment.kafka.enums.ApartmentType;
import com.apartment.kafka.enums.NotificationType;
import com.apartment.notification.core.enums.NotificationStatus;
import com.apartment.notification.core.models.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface NotificationRepository  extends JpaRepository<Notification, UUID> {
    @Query("select n from Notification n where n.status IN :statuses and n.type IN :types")
    List<Notification> getAllByTypes(@Param("statuses") Set<NotificationStatus> statuses, @Param("types")  Set<NotificationType> types);
}
