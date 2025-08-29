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

    @DecimalMin("0.0")
    @Column(name = "min_sessions_per_week", nullable = false)
    private double minSessionsPerWeek;

    @DecimalMin("0.0")
    @Column(name = "max_sessions_per_week", nullable = false)
    private double maxSessionsPerWeek;

    @DecimalMin("0.0")
    @DecimalMax("1.0")
    @Column(name = "discount", nullable = false, precision = 4, scale = 2)
    private Double discount;
}
