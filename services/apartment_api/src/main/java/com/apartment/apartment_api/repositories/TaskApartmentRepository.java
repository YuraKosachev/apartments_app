package com.apartment.apartment_api.repositories;

import com.apartment.apartment_api.core.models.entities.TaskApartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface TaskApartmentRepository extends JpaRepository<TaskApartment, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM TaskApartment ta WHERE ta.apartment.id = :id")
    void deleteAllByApartmentId(@Param("id") UUID id);
}
