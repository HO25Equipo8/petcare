package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.OfferingRepository;
import com.petcare.back.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateBookingOfferingRoleCoverageTest {

    @Mock
    OfferingRepository offeringRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    ValidateBookingOfferingRoleCoverage validator;

    @Test
    void shouldPassWhenProfessionalHasRequiredRole() throws MyException {
        Offering offering = new Offering(); offering.setId(1L); offering.setAllowedRole(ProfessionalRoleEnum.PASEADOR);
        offering.setName(OfferingEnum.PASEO);

        User prof = new User(); prof.setId(10L); prof.setProfessionalRoles(List.of(ProfessionalRoleEnum.PASEADOR));

        BookingCreateDTO dto = new BookingCreateDTO(1L, 1L, null, null, List.of(1L), List.of(10L));

        when(offeringRepository.findById(1L)).thenReturn(Optional.of(offering));
        when(userRepository.findAllById(List.of(10L))).thenReturn(List.of(prof));

        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenNoProfessionalHasRequiredRole() {
        Offering offering = new Offering(); offering.setId(1L); offering.setAllowedRole(ProfessionalRoleEnum.PASEADOR);
        offering.setName(OfferingEnum.PASEO);

        User prof = new User(); prof.setId(10L); prof.setProfessionalRoles(List.of(ProfessionalRoleEnum.CUIDADOR));

        BookingCreateDTO dto = new BookingCreateDTO(1L, 1L, null, null, List.of(1L), List.of(10L));

        when(offeringRepository.findById(1L)).thenReturn(Optional.of(offering));
        when(userRepository.findAllById(List.of(10L))).thenReturn(List.of(prof));

        assertThrows(MyException.class, () -> validator.validate(dto));
    }
}

