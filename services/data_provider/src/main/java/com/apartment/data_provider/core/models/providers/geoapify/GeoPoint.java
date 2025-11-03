package com.apartment.data_provider.core.models.providers.geoapify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeoPoint {
    Datasource datasource;
    String country;
    @JsonProperty("country_code")
    String countryCode;
    String name;
    String county;
    String city;
    String postcode;
    String district;
    String neighbourhood;
    String street;
    String housenumber;
    @JsonProperty("iso3166_2")
    String iso;
    double lon;
    double lat;
    double distance;
    @JsonProperty("result_type")
    String resultType;
    String state;
    @JsonProperty("state_code")
    String stateCode;
    String formatted;
    @JsonProperty("address_line1")
    String addressLine1;
    @JsonProperty("address_line")
    String addressLine2;
    String category;
    Timezone timezone;
    @JsonProperty("plus_code")
    String plusCode;
    Rank rank;
    @JsonProperty("place_id")
    String placeId;
    Bbox bbox;
}
