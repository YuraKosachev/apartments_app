package com.apartment.data_provider.core.interfaces.apiclients.geoapify;

import com.apartment.data_provider.core.models.providers.geoapify.GeoApifyRequest;
import com.apartment.data_provider.core.models.providers.geoapify.GeoApifyResponse;

import java.io.IOException;
import java.net.URISyntaxException;

public interface GeoApifyGeoCoding {
    GeoApifyResponse geoCoding(GeoApifyRequest request) throws IOException, URISyntaxException, InterruptedException;
}
