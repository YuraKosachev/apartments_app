package com.apartment.kafka.models;

public record PropertyInfo(String address,
                           String userAddress,
                           double latitude,
                           double longitude,
                           String photo,
                           String url,
                           Boolean resale,
                           Integer numberOfRooms,
                           Integer floor,
                           Integer numberOfFloors,
                           Double total,
                           Double living,
                           Double kitchen,
                           String sellerType,
                           String rentType,
                           Boolean owner) {
}
