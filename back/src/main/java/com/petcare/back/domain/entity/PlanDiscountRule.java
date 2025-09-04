package com.petcare.back.domain.entity;

import com.petcare.back.domain.enumerated.CustomerCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "plan_discount_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanDiscountRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "category", nullable = false)
    private CustomerCategory category;

    @Column(name = "min_sessions_per_week", nullable = false)
    private Double minSessionsPerWeek;

    @Column(name = "max_sessions_per_week", nullable = false)
    private Double maxSessionsPerWeek;

    @Column(name = "discount", precision = 10, scale = 2, nullable = false)
    private BigDecimal discount;
}
