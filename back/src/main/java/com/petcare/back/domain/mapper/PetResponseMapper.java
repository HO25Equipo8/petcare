package com.petcare.back.domain.mapper;

import com.petcare.back.domain.dto.response.PetResponseDTO;
import com.petcare.back.domain.entity.Pet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetResponseMapper {
    PetResponseDTO toDto(Pet pet);
}
