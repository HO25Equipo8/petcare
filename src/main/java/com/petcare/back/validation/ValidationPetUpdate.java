package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.PetUpdateDTO;
import com.petcare.back.domain.entity.Pet;
import com.petcare.back.exception.MyException;

public interface ValidationPetUpdate {
    void validate(Pet existingPet, PetUpdateDTO updateData) throws MyException;
}
