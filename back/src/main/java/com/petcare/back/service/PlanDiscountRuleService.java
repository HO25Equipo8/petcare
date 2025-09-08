package com.petcare.back.service;

import com.petcare.back.domain.dto.request.PlanDiscountRuleDTO;
import com.petcare.back.domain.dto.response.PlanDiscountRuleResponseDTO;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanDiscountRuleRepository;
import com.petcare.back.validation.ValidationPlanDiscountRule;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PlanDiscountRuleService {

    private final PlanDiscountRuleRepository repository;
    private final List<ValidationPlanDiscountRule> validationPlanDiscountRules;

    public PlanDiscountRuleResponseDTO createRule(PlanDiscountRuleDTO dto) throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo los profesionales pueden registrar reglas de negocio");
        }

        for (ValidationPlanDiscountRule v : validationPlanDiscountRules) {
            v.validate(dto);
        }

        boolean yaExiste = repository.existsByCategoryAndSitter(dto.category(), user);
        if (yaExiste) {
            throw new MyException("Ya tenés una regla de descuento para esta categoría.");
        }

        PlanDiscountRule entity = new PlanDiscountRule();
        entity.setCategory(dto.category());
        entity.setMinSessionsPerWeek(dto.minSessionsPerWeek());
        entity.setMaxSessionsPerWeek(dto.maxSessionsPerWeek());
        entity.setDiscount(dto.discount());
        entity.setSitter(user);

        PlanDiscountRule saved = repository.save(entity);

        return new PlanDiscountRuleResponseDTO(
                saved.getId(),
                saved.getCategory(),
                saved.getMinSessionsPerWeek(),
                saved.getMaxSessionsPerWeek(),
                saved.getDiscount()
        );
    }

    public List<PlanDiscountRule> getRulesForSitter() throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.SITTER) {
            throw new MyException("Solo los profesionales pueden consultar sus reglas de descuento.");
        }

        return repository.findBySitterId(user.getId());
    }

    public void deleteRule(Long id) throws MyException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        PlanDiscountRule rule = repository.findById(id)
                .orElseThrow(() -> new MyException("La regla no existe."));

        if (!rule.getSitter().getId().equals(user.getId())) {
            throw new MyException("No podés eliminar una regla que no te pertenece.");
        }

        repository.deleteById(id);
    }
}

