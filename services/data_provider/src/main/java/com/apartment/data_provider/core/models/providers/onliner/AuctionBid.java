package com.apartment.data_provider.core.models.providers.onliner;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AuctionBid {
    double amount;
    String currency;
}
