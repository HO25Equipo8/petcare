package com.petcare.back.service;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.domain.entity.Plan;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.PlanType;
import com.petcare.back.domain.mapper.request.PlanCreateMapper;
import com.petcare.back.domain.mapper.response.PlanResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanRepository;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.validation.ValidationPlanCreate;
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
    private final UserRepository userRepository;
    private final List<ValidationPlanCreate> validationPlanCreates;

    /**
     * Crea un nuevo plan de suscripción para la plataforma (solo admins).
     */
    public PlanResponseDTO createPlan(PlanCreateDTO dto) throws MyException {

        for (ValidationPlanCreate v : validationPlanCreates) {
            v.validate(dto);
        }

        Plan plan = planMapper.toEntity(dto);
        plan = planRepository.save(plan);

        return planResponseMapper.toDto(plan);
    }

    /**
     * Permite al usuario autenticado suscribirse a un plan existente.
     */
    public PlanResponseDTO subscribeToPlan(PlanType type) throws MyException {
        User user = getAuthenticatedUser();

        Plan plan = planRepository.findByType(type)
                .orElseThrow(() -> new MyException("El plan solicitado no está disponible"));

        user.setPlan(plan);
        userRepository.save(user);

        return planResponseMapper.toDto(plan);
    }

    /**
     * Devuelve todos los planes disponibles en la plataforma.
     */
    public List<PlanResponseDTO> getAllPlans() {
        return planRepository.findAll().stream()
                .map(planResponseMapper::toDto)
                .toList();
    }

    /**
     * Devuelve el plan actual del usuario autenticado.
     */
    public PlanResponseDTO getMyPlan() throws MyException {
        User user = getAuthenticatedUser();
        Plan plan = user.getPlan();

        if (plan == null) {
            throw new MyException("No tenés un plan asignado");
        }
        return planResponseMapper.toDto(plan);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
