package com.apartment.data_provider.core.models.providers.geoapify;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bbox{
    Double lon1;
    Double lat1;
    Double lon2;
    Double lat2;
}