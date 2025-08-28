package com.petcare.back.service;

import com.petcare.back.domain.dto.request.PetCreateDTO;
import com.petcare.back.domain.dto.response.PetResponseDTO;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.PetCreateMapper;
import com.petcare.back.domain.mapper.response.PetResponseMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PetRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final PetCreateMapper petCreateMapper;
    private final PetResponseMapper petResponseMapper;

    public PetService(PetRepository petRepository,
                      PetCreateMapper petCreateMapper,
                      PetResponseMapper petResponseMapper) {
        this.petRepository = petRepository;
        this.petCreateMapper = petCreateMapper;
        this.petResponseMapper = petResponseMapper;
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
}
