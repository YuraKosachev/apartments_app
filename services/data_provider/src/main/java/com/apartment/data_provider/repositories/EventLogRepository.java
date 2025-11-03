package com.apartment.data_provider.repositories;

import com.apartment.data_provider.core.enums.EventType;
import com.apartment.data_provider.core.models.entities.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;
import java.util.List;

@Repository
public interface EventLogRepository extends JpaRepository<EventLog, UUID> {
    @Query("select l from EventLog l where l.sentAt is null AND l.type IN :types")
    List<EventLog> getAllNotSendByType(Set<EventType> types);
}
