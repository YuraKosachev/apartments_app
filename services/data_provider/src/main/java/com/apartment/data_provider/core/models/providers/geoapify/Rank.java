package com.apartment.data_provider.core.models.providers.geoapify;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Rank {
    double importance;
    double popularity;
}
