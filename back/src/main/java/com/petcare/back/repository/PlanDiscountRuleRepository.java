package com.petcare.back.repository;

import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.enumerated.CustomerCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanDiscountRuleRepository extends JpaRepository<PlanDiscountRule, Long> {
    List<PlanDiscountRule> findByCategory(CustomerCategory category);
}
