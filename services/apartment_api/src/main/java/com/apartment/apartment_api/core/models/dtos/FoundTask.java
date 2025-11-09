package com.apartment.apartment_api.core.models.dtos;

import java.util.UUID;

public record FoundTask(UUID task, UUID apartment) {
}
