package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.BookingServiceItemCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.OfferingRepository;
import com.petcare.back.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        if (data.items() == null || data.items().isEmpty()) return;

        Set<Long> professionalIds = data.items().stream()
                .map(BookingServiceItemCreateDTO::professionalId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, User> professionals = userRepository.findAllById(professionalIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        for (BookingServiceItemCreateDTO item : data.items()) {
            Long offeringId = item.offeringId();
            Long professionalId = item.professionalId();

            if (offeringId == null || professionalId == null) continue;

            Offering offering = offeringRepository.findById(offeringId)
                    .orElseThrow(() -> new MyException("El servicio con ID " + offeringId + " no existe"));

            User professional = professionals.get(professionalId);
            if (professional == null) {
                throw new MyException("El profesional con ID " + professionalId + " no existe");
            }

            ProfessionalRoleEnum requiredRole = offering.getAllowedRole();
            if (!professional.getProfessionalRoles().contains(requiredRole)) {
                throw new MyException("El servicio " + offering.getName().getLabel() +
                        " requiere un profesional con rol " + requiredRole.name() +
                        ", pero " + professional.getName() + " no lo tiene");
            }
        }
    }

}
