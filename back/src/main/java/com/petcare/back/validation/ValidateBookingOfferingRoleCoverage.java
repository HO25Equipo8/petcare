package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.OfferingRepository;
import org.springframework.stereotype.Component;

@Component
public class ValidateBookingOfferingRoleCoverage implements ValidationBooking {

    private final OfferingRepository offeringRepository;

    public ValidateBookingOfferingRoleCoverage(OfferingRepository offeringRepository) {
        this.offeringRepository = offeringRepository;
    }

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        if (data.offeringId() == null) return;

        Offering offering = offeringRepository.findById(data.offeringId())
                .orElseThrow(() -> new MyException("El servicio seleccionado no existe"));

        ProfessionalRoleEnum requiredRole = offering.getAllowedRole();

        boolean cubierto = data.professionals().stream()
                .anyMatch(p -> p.getProfessionalRoles().contains(requiredRole));

        if (!cubierto) {
            throw new MyException("El servicio " + offering.getName().getLabel() +
                    " requiere al menos un profesional con rol " + requiredRole.name());
        }
    }
}
