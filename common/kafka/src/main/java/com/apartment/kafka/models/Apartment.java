package com.apartment.kafka.models;

import com.apartment.kafka.enums.ApartmentType;
import com.apartment.kafka.enums.Event;

import java.time.LocalDateTime;
import java.util.UUID;

public record Apartment(
        UUID id,
        ApartmentType apartmentType,
        int sourceId,
        Price price,
        PropertyInfo propertyInfo,
        //geodata
        GeoInfo geoInfo,
        //geodata
        Event event,
        LocalDateTime createdAt,
        LocalDateTime lastTimeUp){
}