package com.apartment.data_provider.core.models.providers.geoapify;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeoApifyRequest {
    double latitude;
    double longitude;
}
