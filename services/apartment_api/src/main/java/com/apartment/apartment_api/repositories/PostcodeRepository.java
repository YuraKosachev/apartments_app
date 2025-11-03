package com.apartment.apartment_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.apartment.apartment_api.core.models.entities.Postcode;

import java.util.UUID;

@Repository
public interface PostcodeRepository extends JpaRepository<Postcode, UUID> {
}
