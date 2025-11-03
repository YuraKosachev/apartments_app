package com.apartment.data_provider.services;

import com.apartment.data_provider.core.interfaces.apiclients.geoapify.GeoApifyGeoCoding;
import com.apartment.data_provider.core.interfaces.services.GeoApifyService;
import com.apartment.data_provider.core.models.providers.geoapify.GeoApifyRequest;
import com.apartment.data_provider.core.models.providers.geoapify.GeoApifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
@RequiredArgsConstructor
public class GeoApifyServiceImpl implements GeoApifyService {
    private final GeoApifyGeoCoding geoApifyGeoCoding;
    @Override
    public GeoApifyResponse geoCoding(GeoApifyRequest geoApifyRequest) throws IOException, URISyntaxException, InterruptedException {
        return geoApifyGeoCoding.geoCoding(geoApifyRequest);
    }
}
