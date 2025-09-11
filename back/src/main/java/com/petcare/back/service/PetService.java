package com.petcare.back.service;

import com.petcare.back.domain.dto.request.PetCreateDTO;
import com.petcare.back.domain.dto.request.PetUpdateDTO;
import com.petcare.back.domain.dto.response.PetResponseDTO;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.PetCreateMapper;
import com.petcare.back.domain.mapper.request.PetUpdateMapper;
import com.petcare.back.domain.mapper.response.PetResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PetRepository;
import com.petcare.back.validation.ValidationPetUpdate;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final PetCreateMapper petCreateMapper;
    private final PetUpdateMapper petUpdateMapper;
    private final PetResponseMapper petResponseMapper;
    private final List<ValidationPetUpdate> validators;

    public PetService(PetRepository petRepository,
                      PetCreateMapper petCreateMapper,
                      PetUpdateMapper petUpdateMapper,
                      PetResponseMapper petResponseMapper,
                      List<ValidationPetUpdate> validators) {
        this.petRepository = petRepository;
        this.petCreateMapper = petCreateMapper;
        this.petUpdateMapper = petUpdateMapper;
        this.petResponseMapper = petResponseMapper;
        this.validators = validators;
    }

    @Transactional
    public PetResponseDTO createPet(PetCreateDTO dto) throws MyException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.OWNER) {
            throw new MyException("Solo los dueños pueden registrar mascotas");
        }

        Pet pet = petCreateMapper.toEntity(dto);
        pet.setOwner(user);
        pet.setActive(true);
        pet.setCreatedAt(LocalDateTime.now());

        // 🧠 Cálculo de edad según fecha de nacimiento
        if (pet.getBirthDate() != null) {
            int years = Period.between(pet.getBirthDate(), LocalDate.now()).getYears();
            pet.setAge(years);
        }

        Pet savedPet = petRepository.save(pet);
        return petResponseMapper.toDto(savedPet);
    }

    @Transactional
    public PetResponseDTO updatePet(Long petId, PetUpdateDTO dto) throws MyException {

        // 1. Get current user and validate permissions
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (user.getRole() != Role.OWNER && user.getRole() != Role.ADMIN) {
            throw new MyException("Tu rol no permite editar mascotas");
        }

        // 2. Find existing pet and verify it exists
        Pet existingPet = petRepository.findById(petId)
                .orElseThrow(() -> new MyException("Mascota no encontrada"));

        // 3. Verify ownership (only for OWNER role, ADMIN can edit any pet)
        if (user.getRole() == Role.OWNER && !existingPet.getOwner().getId().equals(user.getId())) {
            throw new MyException("No tienes permisos para editar esta mascota");
        }

        // 4. Run all validators
        for (ValidationPetUpdate validator : validators) {
            validator.validate(existingPet, dto);
        }

        // 5. Update the pet with new data (MapStruct will ignore null values)
        petUpdateMapper.updateEntity(existingPet, dto);

        // 6. Recalculate age if birth date was updated
        if (dto.birthDate() != null) {
            int years = Period.between(dto.birthDate(), LocalDate.now()).getYears();
            existingPet.setAge(years);
        }

        // 7. Save and return response (updatedAt will be automatically set by @UpdateTimestamp)
        Pet updatedPet = petRepository.save(existingPet);
        return petResponseMapper.toDto(updatedPet);
    }
}
