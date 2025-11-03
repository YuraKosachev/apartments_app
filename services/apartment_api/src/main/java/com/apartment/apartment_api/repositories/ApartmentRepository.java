package com.apartment.apartment_api.repositories;

import com.apartment.apartment_api.core.models.entities.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, UUID>, JpaSpecificationExecutor<Apartment> {
    @Query("select a from Apartment a LEFT JOIN FETCH a.prices where a.sourceId in :refIds")
    List<Apartment> getAllByRefIds(@Param("refIds") Set<UUID> refIds);
}
