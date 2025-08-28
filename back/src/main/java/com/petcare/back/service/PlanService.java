package com.petcare.back.service;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.domain.entity.Plan;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.PlanCreateMapper;
import com.petcare.back.domain.mapper.response.PlanResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private final PlanCreateMapper planMapper;
    private final PlanResponseMapper planResponseMapper;

    public PlanResponseDTO createPlan(PlanCreateDTO dto) throws MyException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.OWNER) {
            throw new MyException("Solo los dueños pueden seleccionar su plan");
        }

        Plan plan = planMapper.toEntity(dto);

        String generatedName = "Plan " + dto.frequencyEnum().getLabel() + " " + dto.intervalEnum().getLabel();
        plan.setName(generatedName);

        double baseDiscount = switch (dto.intervalEnum()) {
            case SEMANAL -> 0.03;
            case QUINCENAL -> 0.05;
            case MENSUAL -> 0.08;
            case TRIMESTRAL -> 0.10;
            case SEMESTRAL -> 0.13;
            case ANUAL -> 0.20;
        };

        double discountFrecuency = Math.min(0.30, dto.frequencyEnum().getFrequencyPerWeek() * 0.05);

        plan.setTimesPerWeek(dto.frequencyEnum().getFrequencyPerWeek());
        Double totalDiscount = baseDiscount + discountFrecuency;
        plan.setPromotion(totalDiscount > 0.4 ? 0.4 : totalDiscount);

        System.out.println(plan.getPromotion() + "Promocion");
        return planResponseMapper.toDto(planRepository.save(plan));
    }

    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll().stream()
                .map(planResponseMapper::toDto)
                .toList();
    }
}