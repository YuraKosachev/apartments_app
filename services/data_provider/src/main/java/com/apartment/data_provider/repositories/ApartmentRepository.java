package com.apartment.data_provider.repositories;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.apartment.data_provider.core.enums.GeoDataStatus;
import com.apartment.data_provider.core.models.entities.Apartment;
import com.apartment.kafka.enums.ApartmentType;
import com.apartment.kafka.enums.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, UUID> {
    @Query("select a from Apartment a where a.apartmentType IN :types")
    List<Apartment> getAllByTypes(@Param("types") Set<ApartmentType> types);

    @Query("select a from Apartment a where a.event IN :events")
    List<Apartment> getAllInEvents(@Param("events") Set<Event> events);

    @Query("select a from Apartment a where a.geoDataStatus IN :geoStatuses and a.event NOT IN :events")
    List<Apartment> getAllNotInEvents(@Param("events") Set<Event> events,
                                      @Param("geoStatuses") Set<GeoDataStatus> geoStatuses);
}
