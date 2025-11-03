package com.apartment.notification.core.models.entities;

import com.apartment.kafka.enums.NotificationType;
import com.apartment.notification.core.converters.JsonStringArrayConverter;
import com.apartment.notification.core.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID Id;

    String subject;

    @Column(name = "addressees", columnDefinition = "TEXT")
    @Convert(converter = JsonStringArrayConverter.class)
    String[] to;

    @Column(nullable = false, columnDefinition = "TEXT")
    String body;

    @Column( columnDefinition = "TEXT")
    String photo;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    NotificationStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    NotificationType type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_at")
    LocalDateTime sentAt;

    @PrePersist
    protected void onCreate() {
        if(status == null) {
            status = NotificationStatus.NEW;
        }
    }
}
