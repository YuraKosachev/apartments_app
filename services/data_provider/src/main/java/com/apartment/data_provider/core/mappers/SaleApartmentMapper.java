package com.apartment.data_provider.core.mappers;

import com.apartment.data_provider.core.models.entities.Apartment;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentSale;
import org.springframework.stereotype.Component;

@Component
public class SaleApartmentMapper {

    public Apartment toEntity(OnlinerApartmentSale source) {
        if (source == null) return null;
        return Apartment.builder()
                .refId(source.getId())
                .sellerType(source.getSeller().getType())
                .floor(source.getFloor())
                .currency(source.getPrice().getCurrency())
                .createdAt(source.getCreatedAt())
                .photo(source.getPhoto())
                .amount(source.getPrice().getAmount())
                .url(source.getUrl())
                .address(source.getLocation().getAddress())
                .userAddress(source.getLocation().getUserAddress())
                .authorId(source.getAuthorId())
                .kitchen(source.getArea().getKitchen())
                .total(source.getArea().getTotal())
                .living(source.getArea().getLiving())
                .numberOfFloors(source.getNumberOfFloors())
                .numberOfRooms(source.getNumberOfRooms())
                .longitude(source.getLocation().getLongitude())
                .latitude(source.getLocation().getLatitude())
                .lastTimeUp(source.getLastTimeUp())
                .upAvailableIn(source.getUpAvailableIn())
                .resale(source.isResale())
                .build();
    }

    public void toEntity(Apartment source, OnlinerApartmentSale target) {
        if (source == null || target == null) return;
        source.setFloor(target.getFloor());
        source.setCurrency(target.getPrice().getCurrency());
        source.setCreatedAt(target.getCreatedAt());
        source.setPhoto(target.getPhoto());
        source.setAmount(target.getPrice().getAmount());
        source.setUrl(target.getUrl());
        source.setAddress(target.getLocation().getAddress());
        source.setUserAddress(target.getLocation().getUserAddress());
        source.setAuthorId(target.getAuthorId());
        source.setKitchen(target.getArea().getKitchen());
        source.setTotal(target.getArea().getTotal());
        source.setLiving(target.getArea().getLiving());
        source.setNumberOfFloors(target.getNumberOfFloors());
        source.setNumberOfRooms(target.getNumberOfRooms());
        source.setLongitude(target.getLocation().getLongitude());
        source.setLatitude(target.getLocation().getLatitude());
        source.setLastTimeUp(target.getLastTimeUp());
        source.setSellerType(target.getSeller().getType());
        source.setUpAvailableIn(target.getUpAvailableIn());
        source.setResale(target.isResale());
    }
}
