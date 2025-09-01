package com.petcare.back.service;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.domain.entity.Plan;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.PlanCreateMapper;
import com.petcare.back.domain.mapper.response.PlanResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanDiscountRuleRepository;
import com.petcare.back.repository.PlanRepository;
import com.petcare.back.validation.ValidationPlanCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanCreateMapper planMapper;
    private final PlanResponseMapper planResponseMapper;
    private final PlanDiscountRuleRepository planDiscountRuleRepository;
    private final List<ValidationPlanCreate> validationPlanCreates;

    public PlanResponseDTO createPlan(PlanCreateDTO dto) throws MyException {
        User user = getAuthenticatedUser();

        if (user.getRole() != Role.OWNER) {
            throw new MyException("Solo los dueños pueden seleccionar su plan");
        }

        for (ValidationPlanCreate v : validationPlanCreates) {
            v.validate(dto);
        }

        Plan plan = planMapper.toEntity(dto);

        // Generar nombre dinámico
        String generatedName = String.format("Plan %s %s",
                dto.frequencyEnum().getLabel(),
                dto.intervalEnum().getLabel());
        plan.setName(generatedName);

        // Guardamos la frecuencia real como double
        double sessionsPerWeek = dto.frequencyEnum().getFrequencyPerWeek();
        plan.setTimesPerWeek(sessionsPerWeek);

        // Calculamos el descuento usando las reglas configuradas por admin
        BigDecimal discount = calculateDiscountBySessions(sessionsPerWeek);
        plan.setPromotion(discount.doubleValue());

        return planResponseMapper.toDto(planRepository.save(plan));
    }

    /**
     * Busca el descuento aplicable según la cantidad de sesiones/semana
     * sin necesidad de pasar categoría explícitamente (se deduce por rango).
     */
    private BigDecimal calculateDiscountBySessions(double sessionsPerWeek) {
        return planDiscountRuleRepository.findAll().stream()
                .filter(rule -> sessionsPerWeek >= rule.getMinSessionsPerWeek()
                        && sessionsPerWeek <= rule.getMaxSessionsPerWeek())
                .map(PlanDiscountRule::getDiscount)
                .findFirst()
                .orElse(BigDecimal.ZERO);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }

    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll().stream()
                .map(planResponseMapper::toDto)
                .toList();
    }
}
