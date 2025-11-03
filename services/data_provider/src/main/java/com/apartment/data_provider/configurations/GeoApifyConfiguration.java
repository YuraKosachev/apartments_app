package com.apartment.data_provider.configurations;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeoApifyConfiguration {
    @Value("${task.geoapify.url}")
    String url;

    @Value("${task.geoapify.apikey}")
    String apiKey;
}