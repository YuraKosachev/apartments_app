package com.apartment.data_provider.core.models.providers.geoapify;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Datasource {
    String sourcename;
    String attribution;
    String license;
    String url;
}
