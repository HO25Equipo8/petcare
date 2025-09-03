package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Component
public class ValidateComboOfferingNotExisting implements ValidationCombo{
    @Override
    public void validate(ComboOfferingCreateDTO dto, List<Offering> offerings) throws MyException {

        Set<Long> foundIds = offerings.stream().map(Offering::getId).collect(Collectors.toSet());
        List<Long> missingIds = dto.offeringIds().stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new MyException("Los siguientes IDs no corresponden a servicios válidos: " + missingIds);
        }
    }
}
