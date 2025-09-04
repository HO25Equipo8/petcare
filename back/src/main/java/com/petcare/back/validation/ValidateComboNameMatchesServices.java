package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.ComboEnum;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidateComboNameMatchesServices implements ValidationCombo {

    @Override
    public void validate(ComboOfferingCreateDTO dto, List<Offering> offerings) throws MyException {
        ComboEnum comboEnum = dto.name();

        Set<OfferingEnum> expectedTypes = comboEnum.getExpectedTypes();
        Set<OfferingEnum> actualTypes = offerings.stream()
                .map(Offering::getName)
                .collect(Collectors.toSet());

        for (OfferingEnum expected : expectedTypes) {
            if (!actualTypes.contains(expected)) {
                throw new MyException("El combo '" + comboEnum.name() + "' debería incluir servicios de tipo '" + expected + "', pero no se encontraron.");
            }
        }

        System.out.println("✅ Validación semántica del combo '" + comboEnum.name() + "' superada.");
    }
}
