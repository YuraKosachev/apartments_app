package com.apartment.kafka.models;

public record GeoInfo(
        Integer postCode,
        String country,
        String neighbourhood,
        String county,
        String street,
        String countryCode,
        String city,
        String district,
        String state,
        String stateCode,
        String formatted,
        String houseNumber,
        Double boxLon1,
        Double boxLat1,
        Double boxLon2,
        Double boxLat2
) {
}
