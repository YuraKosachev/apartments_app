package com.apartment.apartment_api.repositories;

import com.apartment.apartment_api.core.models.entities.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface StreetRepository extends JpaRepository<Street, UUID> {
}
