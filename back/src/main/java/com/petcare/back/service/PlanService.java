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

        if (user.getRole() != Role.ADMIN) {
            throw new MyException("Solo los admin pueden registrar planes");
        }

        Plan plan = planMapper.toEntity(dto);

        String generatedName = "Plan " + dto.timesPerWeek() + " veces por semana/" + dto.intervalEnum().getLabel();
        plan.setName(generatedName);

        double baseDiscount = switch (dto.intervalEnum()) {
            case SEMANAL -> 0.05;
            case QUINCENAL -> 0.10;
            case MENSUAL -> 0.15;
            case TRIMESTRAL -> 0.20;
            case SEMESTRAL -> 0.25;
            case ANUAL -> 0.30;
        };

        double extra = dto.timesPerWeek() >= 5 ? 0.05 : 0.0; // Ejemplo: más de 5 sesiones por semana
        plan.setPromotion(baseDiscount + extra);

        return planResponseMapper.toDto(planRepository.save(plan));
    }

    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll().stream()
                .map(planResponseMapper::toDto)
                .toList();
    }
}