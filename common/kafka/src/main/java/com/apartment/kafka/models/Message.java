package com.apartment.kafka.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    String photo;
    String url;
    String[] prices;
    String[] description;
    String[] message;
}
