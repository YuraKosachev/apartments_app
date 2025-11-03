package com.apartment.apartment_api.core.models.dtos;

import com.apartment.apartment_api.core.models.entities.TaskApartment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskDto {
    UUID id;
    String predicate;
    String name;
    @JsonProperty("issuer_id")
    String issuerId;
}
