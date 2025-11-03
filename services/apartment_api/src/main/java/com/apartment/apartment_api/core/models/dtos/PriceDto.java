package com.apartment.apartment_api.core.models.dtos;

import java.time.LocalDateTime;

public record PriceDto(double amount, String currency, LocalDateTime date ) {
}
