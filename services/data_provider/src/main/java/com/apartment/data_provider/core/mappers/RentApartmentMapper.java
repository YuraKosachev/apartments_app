package com.apartment.data_provider.core.mappers;

import com.apartment.data_provider.core.models.entities.Apartment;
import com.apartment.data_provider.core.models.providers.onliner.OnlinerApartmentRent;
import org.springframework.stereotype.Component;

@Component
public class RentApartmentMapper {
    public Apartment toEntity(OnlinerApartmentRent source) {
        if (source == null) return null;
        return Apartment.builder()
                .refId(source.getId())
                .currency(source.getPrice().getCurrency())
                .createdAt(source.getCreatedAt())
                .photo(source.getPhoto())
                .amount(source.getPrice().getAmount())
                .url(source.getUrl())
                .address(source.getLocation().getAddress())
                .userAddress(source.getLocation().getUserAddress())
                .longitude(source.getLocation().getLongitude())
                .latitude(source.getLocation().getLatitude())
                .lastTimeUp(source.getLastTimeUp())
                .upAvailableIn(source.getUpAvailableIn())
                .rentType(source.getRentType())
                .owner(source.getContact().isOwner())
                .build();
    }

    public void toEntity(Apartment source, OnlinerApartmentRent target) {
        if (source == null || target == null) return;
        source.setCurrency(target.getPrice().getCurrency());
        source.setCreatedAt(target.getCreatedAt());
        source.setPhoto(target.getPhoto());
        source.setAmount(target.getPrice().getAmount());
        source.setUrl(target.getUrl());
        source.setAddress(target.getLocation().getAddress());
        source.setUserAddress(target.getLocation().getUserAddress());
        source.setLongitude(target.getLocation().getLongitude());
        source.setLatitude(target.getLocation().getLatitude());
        source.setLastTimeUp(target.getLastTimeUp());
        source.setRentType(target.getRentType());
        source.setUpAvailableIn(target.getUpAvailableIn());
        source.setOwner(target.getContact().isOwner());
    }
}
