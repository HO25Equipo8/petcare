package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.BookingServiceItemCreateDTO;
import com.petcare.back.domain.entity.Offering;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import com.petcare.back.domain.enumerated.Role;
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
import java.util.Set;

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
        // Profesional con rol correcto
        User prof = new User();
        prof.setId(10L);
        prof.setRole(Role.SITTER);
        prof.setProfessionalRoles(List.of(ProfessionalRoleEnum.PASEADOR));

        // Servicio que requiere ese rol
        Offering offering = new Offering();
        offering.setId(1L);
        offering.setName(OfferingEnum.PASEO);
        offering.setAllowedRole(ProfessionalRoleEnum.PASEADOR);

        // Ítem que vincula ambos
        BookingServiceItemCreateDTO item = new BookingServiceItemCreateDTO(1L, 100L, 10L);
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, List.of(item));

        // Mockeo de repositorios
        when(offeringRepository.findById(1L)).thenReturn(Optional.of(offering));
        when(userRepository.findAllById(Set.of(10L))).thenReturn(List.of(prof));

        // Validación sin excepción
        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenProfessionalHasWrongRole() {
        // Profesional con rol incorrecto
        User prof = new User();
        prof.setId(10L);
        prof.setRole(Role.SITTER);
        prof.setProfessionalRoles(List.of(ProfessionalRoleEnum.CUIDADOR)); // ❌ no cumple

        // Servicio que requiere PASEADOR
        Offering offering = new Offering();
        offering.setId(1L);
        offering.setName(OfferingEnum.PASEO);
        offering.setAllowedRole(ProfessionalRoleEnum.PASEADOR);

        // Ítem que vincula ambos
        BookingServiceItemCreateDTO item = new BookingServiceItemCreateDTO(1L, 100L, 10L);
        BookingCreateDTO dto = new BookingCreateDTO(1L, null, List.of(item));

        // Mockeo de repositorios
        when(offeringRepository.findById(1L)).thenReturn(Optional.of(offering));
        when(userRepository.findAllById(Set.of(10L))).thenReturn(List.of(prof));

        // Validación con excepción esperada
        assertThrows(MyException.class, () -> validator.validate(dto));
    }
}
