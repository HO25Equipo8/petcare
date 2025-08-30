package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.exception.MyException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidateComboPetTypeCoherence implements ValidationCombo {

    @Override
    public void validate(ComboOfferingCreateDTO dto, List<Offering> offerings) throws MyException {
        Set<PetTypeEnum> tipos = offerings.stream()
                .flatMap(o -> o.getApplicablePetTypes().stream())
                .collect(Collectors.toSet());

        if (tipos.size() == 1 && tipos.contains(PetTypeEnum.OTRO)) {
            boolean contieneAseo = offerings.stream().anyMatch(o -> o.getName() == OfferingEnum.ASEO);
            if (contieneAseo) {
                throw new MyException("El combo no puede incluir ASEO si es solo para mascotas tipo OTRO.");
            }
        }
    }
}
