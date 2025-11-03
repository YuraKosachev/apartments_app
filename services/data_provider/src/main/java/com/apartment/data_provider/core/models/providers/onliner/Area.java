package com.apartment.data_provider.core.models.providers.onliner;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Area {
    double total;
    double living;
    double kitchen;
}
