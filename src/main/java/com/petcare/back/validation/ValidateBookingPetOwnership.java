package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PetRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ValidateBookingPetOwnership implements ValidationBooking {

    private final PetRepository petRepository;

    public ValidateBookingPetOwnership(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public void validate(BookingCreateDTO data) throws MyException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) auth.getPrincipal();

        // 1. Validar existencia de la mascota
        Pet pet = petRepository.findById(data.petId())
                .orElseThrow(() -> new MyException("La mascota seleccionada no existe"));

        // 2. Validar que pertenezca al dueño autenticado
        if (!pet.getOwner().getId().equals(principal.getId())) {
            throw new MyException("La mascota no pertenece al usuario autenticado");
        }
    }
}