package com.apartment.apartment_api.core.models.entities;


import com.apartment.apartment_api.core.constants.DbTableConstants;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = DbTableConstants.TASK_TABLE_NAME,
        indexes = {
                @Index(name = "uidx_task_predicate_issuer", columnList = "predicate, issuer_id", unique = true)
        }
)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<TaskApartment> found;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    String predicate;

    @Column(name = "issuer_id", nullable = false)
    String issuerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;
}