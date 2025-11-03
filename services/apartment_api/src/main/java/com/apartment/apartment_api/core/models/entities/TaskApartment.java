package com.apartment.apartment_api.core.models.entities;

import com.apartment.apartment_api.core.constants.DbTableConstants;
import com.apartment.apartment_api.core.models.entities.compositkeys.TaskApartmentKey;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = DbTableConstants.TASK_APARTMENT_TABLE_NAME) // <-- важно, чтобы это было
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskApartment {

    @EmbeddedId
    TaskApartmentKey id = new TaskApartmentKey();

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("taskId")
    @JoinColumn(name = "task_id", nullable = false)
    Task task;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("apartmentId")
    @JoinColumn(name = "apartment_id", nullable = false)
    Apartment apartment;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;
}

