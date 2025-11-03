package com.apartment.data_provider.core.models.providers.onliner;

import com.apartment.data_provider.core.models.entities.Apartment;
import com.apartment.data_provider.core.utils.LocalDateTimeWithoutZoneDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class OnlinerApartment {
    int id;
    Location location;
    Price price;
    String photo;
    @JsonProperty("created_at")
    @JsonDeserialize(using = LocalDateTimeWithoutZoneDeserializer.class)
    LocalDateTime createdAt;
    @JsonProperty("last_time_up")
    @JsonDeserialize(using = LocalDateTimeWithoutZoneDeserializer.class)
    LocalDateTime lastTimeUp;
    @JsonProperty("up_available_in")
    int upAvailableIn;
    String url;

    public boolean ToCompare(Apartment apartment) {
        return this.id == apartment.getRefId()
                && this.location.getAddress().equals(apartment.getAddress())
                && this.location.getUserAddress().equals(apartment.getUserAddress())
                && this.location.getLatitude() == apartment.getLatitude()
                && this.location.getLongitude() == apartment.getLongitude()
                && this.price.getAmount() == apartment.getAmount()
                && this.price.getCurrency().equals(apartment.getCurrency())
                && this.photo.equals(apartment.getPhoto())
                && this.createdAt.equals(apartment.getCreatedAt())
                && this.lastTimeUp.equals(apartment.getLastTimeUp())
                //&& this.upAvailableIn == apartment.getUpAvailableIn()
                && this.url.equals(apartment.getUrl());
    }
}