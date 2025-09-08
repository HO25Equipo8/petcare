package com.petcare.back.repository;

import com.petcare.back.domain.entity.IncidentsTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncidentsTableRepository extends JpaRepository<IncidentsTable, Long> {
    Optional<IncidentsTable> findByIncidentId(Long incidentId);
}
