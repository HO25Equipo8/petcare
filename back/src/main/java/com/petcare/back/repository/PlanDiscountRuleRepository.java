package com.petcare.back.repository;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.enumerated.CustomerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanDiscountRuleRepository extends JpaRepository<PlanDiscountRule, Long> {
    Optional<PlanDiscountRule> findByCategory(CustomerCategory category);

    List<PlanDiscountRule> findAllByCategory(CustomerCategory category);
}
