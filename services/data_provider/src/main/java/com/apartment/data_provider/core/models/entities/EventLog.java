package com.apartment.data_provider.core.models.entities;

import com.apartment.data_provider.core.enums.EventType;
import com.apartment.data_provider.core.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="event_logs")
public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Enumerated(EnumType.ORDINAL)
    EventType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    @CreationTimestamp
    @Column(nullable = false, name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime createdAt;

    @Column(nullable = true, name = "sent_at")
    @Temporal(TemporalType.TIMESTAMP)
    LocalDateTime sentAt;
}
