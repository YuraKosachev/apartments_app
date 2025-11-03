package com.apartment.data_provider.core.interfaces.services;

import com.apartment.data_provider.core.models.providers.geoapify.GeoApifyRequest;
import com.apartment.data_provider.core.models.providers.geoapify.GeoApifyResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public interface GeoApifyService {
    GeoApifyResponse geoCoding(GeoApifyRequest geoApifyRequest) throws IOException, URISyntaxException, InterruptedException;
}
