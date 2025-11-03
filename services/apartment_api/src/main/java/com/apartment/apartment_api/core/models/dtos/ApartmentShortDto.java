package com.apartment.apartment_api.core.models.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ApartmentShortDto {
    UUID apartmentId;
    String address;
    String url;
    String photo;
    Double total;
    Double living;
    Double kitchen;
    PriceDto[] prices;
}
