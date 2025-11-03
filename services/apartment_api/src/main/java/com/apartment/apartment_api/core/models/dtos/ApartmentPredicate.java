package com.apartment.apartment_api.core.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ApartmentPredicate(
        @NotNull(message = "predicate is required")
        @NotEmpty(message = "predicate cannot be empty")
        String predicate
) {
}
