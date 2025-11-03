package com.apartment.data_provider.core.models.providers.onliner;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Location {
    String address;
    @JsonProperty("user_address")
    String userAddress;
    double latitude;
    double longitude;
}
