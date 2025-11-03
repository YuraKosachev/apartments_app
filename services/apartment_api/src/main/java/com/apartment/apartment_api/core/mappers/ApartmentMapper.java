package com.apartment.apartment_api.core.mappers;

import com.apartment.apartment_api.core.constants.DateTimeConstants;
import com.apartment.apartment_api.core.models.dtos.ApartmentShortDto;
import com.apartment.apartment_api.core.models.dtos.PriceDto;
import com.apartment.apartment_api.core.models.entities.Apartment;
import com.apartment.apartment_api.core.models.entities.Task;
import com.apartment.kafka.enums.Event;
import com.apartment.kafka.models.Message;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
public class ApartmentMapper {

    public Message toMessage(ApartmentShortDto shortDto) {
        if(shortDto == null) return null;
        return Message.builder()
                .url(shortDto.getUrl())
                .message(shortDto.getAddress())
                .description("кухня - %.2f, жилая - %.2f, общая - %.2f".formatted(shortDto.getKitchen(), shortDto.getLiving(), shortDto.getTotal()))
                .photo(shortDto.getPhoto())
                .prices(Arrays.stream(shortDto.getPrices())
                        .map(item->"%.2f %s %s".formatted(item.amount(), item.currency(), item.date().format(DateTimeFormatter.ofPattern(DateTimeConstants.DATE_FORMAT)))).toArray(String[]::new))
                .build();
    }

    public ApartmentShortDto toApartmentShortDto(Apartment apartment) {
        if(apartment == null) return null;

        return ApartmentShortDto.builder()
                .apartmentId(apartment.getId())
                .address(apartment.getAddress().getFormatted())
                .prices(apartment.getPrices().stream()
                        .map(x-> new PriceDto(x.getAmount(),x.getCurrency(),x.getCreatedAt()))
                        .toArray(PriceDto[]::new))
                .url(apartment.getUrl())
                .photo(apartment.getPhoto())
                .total(apartment.getTotal())
                .living(apartment.getLiving())
                .kitchen(apartment.getKitchen())
                .build();

    }
    public Apartment mapApartmentToApartment(final com.apartment.kafka.models.Apartment source) {
        if(source == null) return null;
        return Apartment.builder()
                .apartmentType(source.apartmentType())
                .floor(source.propertyInfo().floor())
                .owner(source.propertyInfo().owner())
                .photo(source.propertyInfo().photo())
                .url(source.propertyInfo().url())
                .kitchen(source.propertyInfo().kitchen())
                .living(source.propertyInfo().living())
                .total(source.propertyInfo().total())
                .numberOfFloors(source.propertyInfo().numberOfFloors())
                .numberOfRooms(source.propertyInfo().numberOfRooms())
                .lastTimeUp(source.lastTimeUp())
                .resale(source.propertyInfo().resale())
                .rentType(source.propertyInfo().rentType())
                .sourceId(source.id())
                .deletedAt(source.event() == Event.DELETED ? LocalDateTime.now() : null)
                .build();
    }
}
