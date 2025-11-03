package com.apartment.data_provider.core.models.providers.geoapify;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeoApifyResponse {
        List<GeoPoint> results;
        Query query;
}



