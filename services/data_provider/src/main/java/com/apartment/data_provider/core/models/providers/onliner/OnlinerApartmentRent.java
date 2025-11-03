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
public class OnlinerApartmentRent extends OnlinerApartment {
    @JsonProperty("rent_type")
    String rentType;
    Contact contact;

    @Override
    public boolean ToCompare(Apartment apartment) {
        return super.ToCompare(apartment)
                && rentType.equals(apartment.getRentType())
                && contact.isOwner() == apartment.getOwner();
    }
}
