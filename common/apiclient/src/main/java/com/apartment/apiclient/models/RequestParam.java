package com.apartment.apiclient.models;

import com.apartment.apiclient.enums.Param;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestParam {
    String name;
    Object value;
    Param param;
}