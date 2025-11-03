package com.apartment.data_provider.core.mappers;

import com.apartment.data_provider.core.enums.GeoDataStatus;
import com.apartment.kafka.models.Apartment;
import com.apartment.kafka.models.GeoInfo;
import com.apartment.kafka.models.Price;
import com.apartment.kafka.models.PropertyInfo;
import org.springframework.stereotype.Component;

@Component
public class ApartmentMapper {

    public Apartment getApartmentMessage(com.apartment.data_provider.core.models.entities.Apartment entity) {
        if(entity == null) return null;
        return new Apartment(
                entity.getId(),
                entity.getApartmentType(),
                entity.getRefId(),
                getPrice(entity),
                getPropertyInfo(entity),
                getGeoInfo(entity),
                entity.getEvent(),
                entity.getCreatedAt(),
                entity.getLastTimeUp());
    }

    private GeoInfo getGeoInfo(com.apartment.data_provider.core.models.entities.Apartment entity) {
        if(entity == null || entity.getGeoDataStatus() != GeoDataStatus.PROCESSED ) return null;

        return new GeoInfo(
                entity.getPostCode(),
                entity.getCountry(),
                entity.getNeighbourhood(),
                entity.getCounty(),
                entity.getStreet(),
                entity.getCountryCode(),
                entity.getCity(),
                entity.getDistrict(),
                entity.getState(),
                entity.getStateCode(),
                entity.getFormatted(),
                entity.getHouseNumber(),
                entity.getBoxLon1(),
                entity.getBoxLat1(),
                entity.getBoxLon2(),
                entity.getBoxLat2()
        );
    }
    private PropertyInfo getPropertyInfo(com.apartment.data_provider.core.models.entities.Apartment entity){

        if(entity == null) return null;
        return new PropertyInfo(
                entity.getAddress(),
                entity.getUserAddress(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getPhoto(),
                entity.getUrl(),
                entity.getResale(),
                entity.getNumberOfRooms(),
                entity.getFloor(),
                entity.getNumberOfFloors(),
                entity.getTotal(),
                entity.getLiving(),
                entity.getKitchen(),
                entity.getSellerType(),
                entity.getRentType(),
                entity.getOwner());
    }

    private Price getPrice(com.apartment.data_provider.core.models.entities.Apartment entity){
        if(entity == null) return null;
        return new Price(entity.getAmount(), entity.getCurrency());
    }
}
