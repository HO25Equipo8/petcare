package com.petcare.back.repository;

import com.petcare.back.domain.entity.Plan;
import com.petcare.back.domain.enumerated.PlanType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findByType(PlanType type);

    boolean existsByType(PlanType type);
}
