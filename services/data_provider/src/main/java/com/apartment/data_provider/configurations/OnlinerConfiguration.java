package com.apartment.data_provider.configurations;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OnlinerConfiguration {

    @Value("${task.onliner.rent_url}")
    String rentUrl;

    @Value("${task.onliner.sale_url}")
    String saleUrl;
}
