package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.ComboOfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ValidateComboOfferingUniqueness implements ValidationCombo {

    @Autowired
    private ComboOfferingRepository comboOfferingRepository;

    @Override
    public void validate(ComboOfferingCreateDTO dto, List<Offering> offerings) throws MyException {
        boolean exists = comboOfferingRepository.existsByName(dto.name());
        if (exists) {
            throw new MyException("Ya existe un combo registrado con el nombre '" + dto.name() + "'." + "Si quieres modificar buscalo dentro del los ya creados");
        }

        // Validación interna: no repetir servicios dentro del mismo combo
        Set<Long> ids = new HashSet<>(dto.offeringIds());
        if (ids.size() < dto.offeringIds().size()) {
            throw new MyException("El combo contiene servicios repetidos.");
        }
    }
}
