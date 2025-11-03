package com.apartment.data_provider.tasks.base;

import com.apartment.data_provider.core.enums.GeoDataStatus;
import com.apartment.data_provider.core.interfaces.services.GeoApifyService;
import com.apartment.data_provider.core.mappers.GeoApartmentMapper;
import com.apartment.data_provider.core.models.entities.Apartment;
import com.apartment.data_provider.core.models.providers.geoapify.GeoApifyRequest;

public abstract class BaseApartmentTask {
    private final GeoApifyService geoApifyService;
    private final GeoApartmentMapper geoApartmentMapper;

    public BaseApartmentTask(GeoApifyService geoApifyService, GeoApartmentMapper geoApartmentMapper) {
        this.geoApifyService = geoApifyService;
        this.geoApartmentMapper = geoApartmentMapper;
    }

    protected void setGeoData(Apartment entity) {
        try {
            var geoRequest = GeoApifyRequest
                    .builder()
                    .longitude(entity.getLongitude())
                    .latitude(entity.getLatitude())
                    .build();
            var geoResponse = geoApifyService.geoCoding(geoRequest);
            if(geoResponse != null) {
                geoApartmentMapper.merge(entity, geoResponse.getResults().get(0));
                entity.setGeoDataStatus(GeoDataStatus.PROCESSED);
            }
        } catch (Exception e) {
            entity.setGeoDataStatus(GeoDataStatus.ERROR);
        }
    }
}
