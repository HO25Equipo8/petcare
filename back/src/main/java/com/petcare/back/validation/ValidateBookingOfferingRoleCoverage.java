package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.OfferingRepository;
import com.petcare.back.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidateBookingOfferingRoleCoverage implements ValidationBooking {

    private final OfferingRepository offeringRepository;
    private final UserRepository userRepository;

    public ValidateBookingOfferingRoleCoverage(OfferingRepository offeringRepository,
                                               UserRepository userRepository) {
        this.offeringRepository = offeringRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        if (data.offeringId() == null || data.professionals() == null) return;

        Offering offering = offeringRepository.findById(data.offeringId())
                .orElseThrow(() -> new MyException("El servicio seleccionado no existe"));

        ProfessionalRoleEnum requiredRole = offering.getAllowedRole();

        List<User> professionals = userRepository.findAllById(data.professionals());

        boolean cubierto = professionals.stream()
                .anyMatch(p -> p.getProfessionalRoles().contains(requiredRole));

        if (!cubierto) {
            throw new MyException("El servicio " + offering.getName().getLabel() +
                    " requiere al menos un profesional con rol " + requiredRole.name());
        }
    }
}
