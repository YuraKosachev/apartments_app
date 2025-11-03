package com.apartment.data_provider.core.models.providers.onliner;

import com.apartment.data_provider.core.models.entities.Apartment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OnlinerApartmentSale extends OnlinerApartment {
    @JsonProperty("author_id")
    int authorId;
    boolean resale;
    @JsonProperty("number_of_rooms")
    int numberOfRooms;
    int floor;
    @JsonProperty("number_of_floors")
    int numberOfFloors;
    Area area;
    Seller seller;
    @JsonProperty("auction_bid")
    AuctionBid auctionBid;

    @Override
    public boolean ToCompare(Apartment apartment) {
        return super.ToCompare(apartment)
                && apartment.getAuthorId() == authorId
                && apartment.getNumberOfRooms() == numberOfRooms
                && resale == apartment.getResale()
                && floor == apartment.getFloor()
                && numberOfFloors == apartment.getNumberOfFloors()
                && area.getKitchen() == apartment.getKitchen()
                && area.getLiving() == apartment.getLiving()
                && area.getTotal() == apartment.getTotal()
                && seller.getType().equals(apartment.getSellerType());

    }
}
