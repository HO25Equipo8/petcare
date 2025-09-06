package com.petcare.back.repository;

import com.petcare.back.domain.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    boolean existsByName(String generatedName);

    Optional<Plan> findByOwnerId(Long id);
}
