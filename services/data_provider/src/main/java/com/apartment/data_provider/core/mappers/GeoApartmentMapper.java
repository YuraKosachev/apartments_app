package com.apartment.data_provider.core.mappers;
import com.apartment.data_provider.core.models.entities.Apartment;
import com.apartment.data_provider.core.models.providers.geoapify.GeoPoint;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

@Component
public class GeoApartmentMapper {
    public void merge(Apartment source, GeoPoint point) {
        if(source == null || point == null) {
            return;
        }
        if(point.getPostcode() != null) {
            source.setPostCode(Integer.parseInt(point.getPostcode()));
        }
        source.setCountry(point.getCountry());
        source.setNeighbourhood(point.getNeighbourhood());
        source.setCounty(point.getCounty());
        source.setCountryCode(point.getCountryCode());
        source.setCity(point.getCity());
        source.setDistrict(point.getDistrict());
        source.setHouseNumber(point.getHousenumber());

        if(point.getBbox() != null) {
            source.setBoxLon1(point.getBbox().getLon1());
            source.setBoxLat1(point.getBbox().getLat1());
            source.setBoxLon2(point.getBbox().getLon2());
            source.setBoxLat2(point.getBbox().getLat2());
        }
        source.setState(point.getState());
        source.setFormatted(point.getFormatted());
        source.setStateCode(point.getStateCode());
        source.setStreet(point.getStreet());
    }
}
