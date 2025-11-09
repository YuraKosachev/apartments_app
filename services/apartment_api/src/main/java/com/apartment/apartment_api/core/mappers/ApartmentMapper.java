package com.apartment.apartment_api.core.mappers;

import com.apartment.apartment_api.core.constants.DateTimeConstants;
import com.apartment.apartment_api.core.models.dtos.ApartmentShortDto;
import com.apartment.apartment_api.core.models.dtos.PriceDto;
import com.apartment.apartment_api.core.models.entities.Apartment;
import com.apartment.apartment_api.core.models.entities.Task;
import com.apartment.kafka.enums.ApartmentType;
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
        if (shortDto == null) return null;
        String description = shortDto.getApartmentType() == ApartmentType.SALE
                ? "комнаты - %d, этаж - %d,\n\u00A0\u00A0\u00A0\u00A0кухня - %.2f, жилая - %.2f,\n\u00A0\u00A0\u00A0\u00A0общая - %.2f".formatted(shortDto.getRooms(),
                    shortDto.getFloor(),
                    shortDto.getKitchen(),
                    shortDto.getLiving(),
                    shortDto.getTotal())
                : null;
        return Message.builder()
                .url(shortDto.getUrl())
                .message(shortDto.getAddress())
                .description(description)
                .photo(shortDto.getPhoto())
                .prices(getFormattedPrices(shortDto.getPrices()))
                .build();
    }


    private String[] getFormattedPrices(PriceDto[] prices) {
        if (prices == null || prices.length == 0) {
            return new String[0];
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateTimeConstants.DATE_FORMAT);
        String baseFormat = "%.2f %s %s";
        String upIcon = " 🔺";
        String downIcon = " 🔻";

        String[] formattedPrices = new String[prices.length];

        for (int i = 0; i < prices.length; i++) {
            PriceDto current = prices[i];
            String icon = "";

            if (i > 0) {
                double prevAmount = prices[i - 1].amount();
                if (current.amount() > prevAmount) {
                    icon = " " + upIcon;
                } else if (current.amount() < prevAmount) {
                    icon = " " + downIcon;
                }
            }

            formattedPrices[i] = String.format(
                    baseFormat + "%s",
                    current.amount(),
                    current.currency(),
                    current.date().format(formatter),
                    icon
            );
        }

        return formattedPrices;
    }



    public ApartmentShortDto toApartmentShortDto(Apartment apartment) {
        if (apartment == null) return null;

        return ApartmentShortDto.builder()
                .apartmentId(apartment.getId())
                .address(apartment.getAddress().getFormatted())
                .prices(apartment.getPrices().stream()
                        .map(x -> new PriceDto(x.getAmount(), x.getCurrency(), x.getCreatedAt()))
                        .toArray(PriceDto[]::new))
                .apartmentType(apartment.getApartmentType())
                .rooms(apartment.getNumberOfRooms())
                .floor(apartment.getFloor())
                .owner(apartment.getOwner())
                .url(apartment.getUrl())
                .photo(apartment.getPhoto())
                .total(apartment.getTotal())
                .living(apartment.getLiving())
                .kitchen(apartment.getKitchen())
                .build();

    }

    public Apartment mapApartmentToApartment(final com.apartment.kafka.models.Apartment source) {
        if (source == null) return null;
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
