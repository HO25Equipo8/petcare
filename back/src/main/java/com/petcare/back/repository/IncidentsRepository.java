package com.petcare.back.repository;

import com.petcare.back.domain.entity.Incidents;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncidentsRepository extends JpaRepository<Incidents, Long> {

}
