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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    // ==================== Basic Fields Validation Tests ====================

    @Test
    void shouldThrowIfNameIsTooShort() throws MyException {
        Long petId = 1L;
        Pet existingPet = createMockPet();
        PetUpdateDTO dto = new PetUpdateDTO(
                "X", PetTypeEnum.PERRO, null, null, null, null, null,
                null, null, null, null, null, null, null, null
        );

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        doThrow(new MyException("El nombre de la mascota debe tener al menos 2 caracteres"))
                .when(basicFieldsValidator).validate(existingPet, dto);

        MyException ex = assertThrows(MyException.class, () ->
                petService.updatePet(petId, dto)
        );
        assertEquals("El nombre de la mascota debe tener al menos 2 caracteres", ex.getMessage());
    }

    @Test
    void shouldThrowIfWeightIsTooLow() throws MyException {
        Long petId = 1L;
        Pet existingPet = createMockPet();
        PetUpdateDTO dto = new PetUpdateDTO(
                null, null, null, 0.05, null, null, null,
                null, null, null, null, null, null, null, null
        );

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        doThrow(new MyException("El peso debe ser mayor a 0.1kg"))
                .when(basicFieldsValidator).validate(existingPet, dto);

        MyException ex = assertThrows(MyException.class, () ->
                petService.updatePet(petId, dto)
        );
        assertEquals("El peso debe ser mayor a 0.1kg", ex.getMessage());
    }

    // ==================== Weight-Size Validation Tests ====================

    @Test
    void shouldThrowIfWeightDoesNotMatchSize() throws MyException {
        Long petId = 1L;
        Pet existingPet = createMockPet();
        PetUpdateDTO dto = new PetUpdateDTO(
                null, null, null, 50.0, null,
                PetSizeEnum.SMALL, null, // 50kg for SMALL pet
                null, null, null, null, null, null, null, null
        );

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        doThrow(new MyException("El peso 50.0kg no es realista para una mascota de tamaño SMALL (rango esperado: 0.5kg - 10kg)"))
                .when(weightSizeValidator).validate(existingPet, dto);

        MyException ex = assertThrows(MyException.class, () ->
                petService.updatePet(petId, dto)
        );
        assertTrue(ex.getMessage().contains("El peso 50.0kg no es realista para una mascota de tamaño SMALL"));
    }

    // ==================== Authorization Tests ====================

    @Test
    void shouldThrowIfUserIsNotOwnerOrAdmin() throws MyException {
        Long petId = 1L;
        Pet existingPet = createMockPet();
        existingPet.getOwner().setId(2L); // Different owner

        // Setup user with OWNER role but different ID
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setRole(Role.OWNER);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(mockUser, null)
        );

        PetUpdateDTO dto = new PetUpdateDTO(
                "Max", null, null, null, null, null, null,
                null, null, null, null, null, null, null, null
        );

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));

        MyException ex = assertThrows(MyException.class, () ->
                petService.updatePet(petId, dto)
        );
        assertEquals("No tienes permisos para editar esta mascota", ex.getMessage());
    }

    @Test
    void shouldAllowAdminToEditAnyPet() throws MyException {
        Long petId = 1L;
        Pet existingPet = createMockPet();
        existingPet.getOwner().setId(2L); // Different owner

        // Setup ADMIN user
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setRole(Role.ADMIN);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminUser, null)
        );

        PetUpdateDTO dto = new PetUpdateDTO(
                "Max Updated", null, null, null, null, null, null,
                null, null, null, null, null, null, null, null
        );

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));
        doNothing().when(birthDateValidator).validate(existingPet, dto);
        doNothing().when(basicFieldsValidator).validate(existingPet, dto);
        doNothing().when(weightSizeValidator).validate(existingPet, dto);

        when(petRepository.save(existingPet)).thenReturn(existingPet);

        // Should not throw exception
        assertDoesNotThrow(() -> petService.updatePet(petId, dto));

        verify(petUpdateMapper).updateEntity(existingPet, dto);
        verify(petRepository).save(existingPet);
    }

    @Test
    void shouldThrowIfPetNotFound() {
        Long petId = 999L;
        PetUpdateDTO dto = new PetUpdateDTO(
                "Max", null, null, null, null, null, null,
                null, null, null, null, null, null, null, null
        );

        when(petRepository.findById(petId)).thenReturn(Optional.empty());

        MyException ex = assertThrows(MyException.class, () ->
                petService.updatePet(petId, dto)
        );
        assertEquals("Mascota no encontrada", ex.getMessage());
    }

    // ==================== Success Cases ====================

    @Test
    void shouldPassIfAllValidatorsApprove() throws MyException {
        Long petId = 1L;
        Pet existingPet = createMockPet();
        PetUpdateDTO dto = new PetUpdateDTO(
                "Max Updated", PetTypeEnum.PERRO, "Golden Retriever", 30.0, "Golden",
                PetSizeEnum.LARGE, LocalDate.now().minusYears(5), "123456789012345",
                null, null, null, null, null, null, "1234567890"
        );

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));

        // All validators pass
        doNothing().when(birthDateValidator).validate(existingPet, dto);
        doNothing().when(basicFieldsValidator).validate(existingPet, dto);
        doNothing().when(weightSizeValidator).validate(existingPet, dto);

        when(petRepository.save(existingPet)).thenReturn(existingPet);

        assertDoesNotThrow(() -> petService.updatePet(petId, dto));

        // Verify all validators were called
        verify(birthDateValidator).validate(existingPet, dto);
        verify(basicFieldsValidator).validate(existingPet, dto);
        verify(weightSizeValidator).validate(existingPet, dto);

        // Verify mapping and saving
        verify(petUpdateMapper).updateEntity(existingPet, dto);
        verify(petRepository).save(existingPet);
    }

    @Test
    void shouldRecalculateAgeWhenBirthDateUpdated() throws MyException {
        Long petId = 1L;
        Pet existingPet = createMockPet();
        LocalDate newBirthDate = LocalDate.now().minusYears(3).minusMonths(6);
        PetUpdateDTO dto = new PetUpdateDTO(
                null, null, null, null, null, null, newBirthDate,
                null, null, null, null, null, null, null, null
        );

        when(petRepository.findById(petId)).thenReturn(Optional.of(existingPet));

        doNothing().when(birthDateValidator).validate(existingPet, dto);
        doNothing().when(basicFieldsValidator).validate(existingPet, dto);
        doNothing().when(weightSizeValidator).validate(existingPet, dto);

        when(petRepository.save(existingPet)).thenReturn(existingPet);

        petService.updatePet(petId, dto);

        // Verify age was recalculated (should be 3 years)
        assertEquals(3, existingPet.getAge());
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
