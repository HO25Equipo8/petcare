package com.petcare.back.repository;

import com.petcare.back.domain.entity.Incidents;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IncidentsRepository extends JpaRepository<Incidents, Long> {

    Optional<Incidents> getIncidentsById(long incidentId);
}
