package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.OfferingRepository;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class ValidateOfferingUniqueness implements ValidationOffering {

    private final OfferingRepository offeringRepository;

    public ValidateOfferingUniqueness(OfferingRepository offeringRepository) {
        this.offeringRepository = offeringRepository;
    }

    @Override
    public void validar(OfferingCreateDTO dto) throws MyException {
        List<Offering> existing = offeringRepository.findByName(dto.name());

        for (Offering o : existing) {
            List<PetTypeEnum> existingTypes = o.getApplicablePetTypes();
            List<PetTypeEnum> incomingTypes = dto.applicablePetTypes();

            if (new HashSet<>(existingTypes).containsAll(incomingTypes) && new HashSet<>(incomingTypes).containsAll(existingTypes)) {
                throw new MyException("Ya existe un servicio con ese nombre y tipo de mascota.");
            }
        }
    }
}

