package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PetUpdateDTO;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.enumerated.PetSizeEnum;
import com.petcare.back.domain.enumerated.PetTypeEnum;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.PetUpdateMapper;
import com.petcare.back.domain.mapper.response.PetResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PetRepository;
import com.petcare.back.service.PetService;
import com.petcare.back.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PetUpdateValidationServiceTest {

    @Mock private PetRepository petRepository;
    @Mock private PetUpdateMapper petUpdateMapper;
    @Mock private PetResponseMapper petResponseMapper;

    @Mock(lenient = true) private ValidatePetUpdateBirthDate birthDateValidator;
    @Mock(lenient = true) private ValidatePetUpdateBasicFields basicFieldsValidator;
    @Mock(lenient = true) private ValidatePetUpdateWeightSize weightSizeValidator;

    private PetService petService;

    @BeforeEach
    void setup() {
        List<ValidationPetUpdate> validators = List.of(
                birthDateValidator,
                basicFieldsValidator,
                weightSizeValidator
        );

        petService = new PetService(
                petRepository,
                null, // petCreateMapper not needed for update tests
                petUpdateMapper,
                petResponseMapper,
                validators
        );

        // Setup authenticated user
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Role.OWNER);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockUser, null)
        );
    }

    // ==================== Birth Date Validation Tests ====================

    @Test
    void shouldThrowIfBirthDateIsInFuture() throws MyException {
        Long petId = 1L;
        Pet existingPet = createMockPet();
        PetUpdateDTO dto = new PetUpdateDTO(
                "Max", PetTypeEnum.PERRO, "Labrador", 25.0, "Golden",
                PetSizeEnum.LARGE, LocalDate.now().plusDays(1), // Future date
                null, null, null, null, null, null, null, null
        );

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        doThrow(new MyException("La fecha de nacimiento no puede ser futura"))
                .when(birthDateValidator).validate(existingPet, dto);

        MyException ex = assertThrows(MyException.class, () ->
                petService.updatePet(petId, dto)
        );
        assertEquals("La fecha de nacimiento no puede ser futura", ex.getMessage());
    }

    @Test
    void shouldThrowIfBirthDateIsTooOld() throws MyException {
        Long petId = 1L;
        Pet existingPet = createMockPet();
        PetUpdateDTO dto = new PetUpdateDTO(
                "Max", PetTypeEnum.PERRO, "Labrador", 25.0, "Golden",
                PetSizeEnum.LARGE, LocalDate.now().minusYears(35), // Too old
                null, null, null, null, null, null, null, null
        );

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        doThrow(new MyException("La fecha de nacimiento es demasiado antigua (máximo 30 años)"))
                .when(birthDateValidator).validate(existingPet, dto);

        MyException ex = assertThrows(MyException.class, () ->
                petService.updatePet(petId, dto)
        );
        assertEquals("La fecha de nacimiento es demasiado antigua (máximo 30 años)", ex.getMessage());
    }

    // ==================== Helper Methods ====================

    private Pet createMockPet() {
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setName("Original Name");
        pet.setType(PetTypeEnum.PERRO);
        pet.setBreed("Original Breed");
        pet.setAge(5);
        pet.setWeight(25.0);
        pet.setPetSize(PetSizeEnum.LARGE);
        pet.setBirthDate(LocalDate.now().minusYears(5));
        pet.setActive(true);
        pet.setCreatedAt(LocalDateTime.now().minusMonths(6));

        User owner = new User();
        owner.setId(1L);
        owner.setRole(Role.OWNER);
        pet.setOwner(owner);

        return pet;
    }
}
