package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateBookingPetOwnershipTest {

    @Mock
    PetRepository petRepository;
    @InjectMocks
    ValidateBookingPetOwnership validator;

    @Test
    void shouldPassWhenPetBelongsToOwner() throws MyException {
        User owner = new User(); owner.setId(1L);
        Pet pet = new Pet(); pet.setId(10L); pet.setOwner(owner);

        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(owner, null));
        BookingCreateDTO dto = new BookingCreateDTO(10L, null, null, List.of(1L), List.of());

        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));

        assertDoesNotThrow(() -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenPetDoesNotExist() {
        BookingCreateDTO dto = new BookingCreateDTO(99L, null, null, List.of(1L), List.of());
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MyException.class, () -> validator.validate(dto));
    }

    @Test
    void shouldThrowWhenPetDoesNotBelongToOwner() {
        User owner = new User(); owner.setId(1L);
        User other = new User(); other.setId(2L);
        Pet pet = new Pet(); pet.setId(10L); pet.setOwner(other);

        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(owner, null));
        BookingCreateDTO dto = new BookingCreateDTO(10L, null, null, List.of(1L), List.of());

        when(petRepository.findById(10L)).thenReturn(Optional.of(pet));

        assertThrows(MyException.class, () -> validator.validate(dto));
    }
}
