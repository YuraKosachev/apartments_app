package com.apartment.apartment_api.core.models.entities.compositkeys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;
@Embeddable
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskApartmentKey implements Serializable {

    @Column(name = "task_id", nullable = false)
    UUID taskId;

    @Column(name = "apartment_id", nullable = false)
    UUID apartmentId;
}