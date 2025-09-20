package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.OfferingVariantDescriptionEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.OfferingRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
public class ValidateOfferingUniqueness implements ValidationOffering {

    private final OfferingRepository offeringRepository;

    public ValidateOfferingUniqueness(OfferingRepository offeringRepository) {
        this.offeringRepository = offeringRepository;
    }

    @Override
    public void validate(OfferingCreateDTO dto) throws MyException {
        List<Offering> existing = offeringRepository.findByName(dto.name());

        for (Offering o : existing) {
            boolean mismaMascota = new HashSet<>(o.getApplicablePetTypes()).equals(new HashSet<>(dto.applicablePetTypes()));
            boolean mismaDescripcion = o.getDescription().equalsIgnoreCase(dto.description());

            if (mismaMascota && mismaDescripcion) {
                throw new MyException("Ya existe un servicio con ese nombre, tipo de mascota y descripción.");
            }
        }

        boolean descripcionValida = Arrays.stream(OfferingVariantDescriptionEnum.values())
                .anyMatch(v -> v.getBaseOffering() == dto.name() && v.getDescription().equalsIgnoreCase(dto.description()));

        if (!descripcionValida) {
            throw new MyException("La descripción no corresponde a una variante válida para el servicio " + dto.name().name() + ".");
        }
    }
}

