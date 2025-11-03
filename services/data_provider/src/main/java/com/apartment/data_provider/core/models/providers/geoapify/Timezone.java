package com.apartment.data_provider.core.models.providers.geoapify;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Timezone {
    String name;

    @JsonProperty("offset_STD")
    String offsetSTD;

    @JsonProperty("offset_STD_seconds")
    int offsetSTDSeconds;

    @JsonProperty("offset_DST")
    String offsetDST;

    @JsonProperty("offset_DST_seconds")
    int offsetDSTSeconds;
}
