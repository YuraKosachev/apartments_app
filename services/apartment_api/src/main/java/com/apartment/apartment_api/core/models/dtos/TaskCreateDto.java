package com.apartment.apartment_api.core.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record TaskCreateDto(
        @NotNull(message = "predicate is required")
        @NotEmpty(message = "predicate cannot be empty")
        String predicate,

        @NotNull(message = "name is required")
        @NotEmpty(message = "name cannot be empty")
        String name,

        @JsonProperty("chat_id")
        @NotNull(message = "chat_id is required")
        String chatId) {
}
