package com.apartment.apartment_api.core.models.dtos;

import com.apartment.kafka.enums.ApartmentType;
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
    ApartmentType apartmentType;
    String url;
    String photo;
    Boolean owner;
    Integer floor;
    Integer rooms;
    Double total;
    Double living;
    Double kitchen;
    PriceDto[] prices;
}
