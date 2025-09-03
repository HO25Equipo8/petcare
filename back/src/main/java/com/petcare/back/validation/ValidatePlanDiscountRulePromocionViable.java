package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanDiscountRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ValidatePlanDiscountRulePromocionViable implements ValidationPlanDiscountRule {

    private final PlanDiscountRuleRepository ruleRepository;

    @Override
    public void validate(PlanDiscountRule rule) throws MyException {
        BigDecimal discount = rule.getDiscount();

        // Validación extra: coherencia con el rango
        if (rule.getMinSessionsPerWeek() <= 0 || rule.getMaxSessionsPerWeek() < rule.getMinSessionsPerWeek()) {
            throw new MyException("El rango de sesiones no es válido para aplicar una promoción.");
        }

        // Opcional: verificar que el descuento tenga sentido comercial
        if (discount.compareTo(BigDecimal.valueOf(50)) > 0 && rule.getMaxSessionsPerWeek() < 2) {
            throw new MyException("Una promoción tan alta no debería aplicarse a clientes poco frecuentes.");
        }
    }
}
