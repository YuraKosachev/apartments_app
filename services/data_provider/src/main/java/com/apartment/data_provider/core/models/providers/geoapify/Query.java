package com.apartment.data_provider.core.models.providers.geoapify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Query{
    double lat;
    double lon;

    @JsonProperty("plus_code")
    String plusCode;
}