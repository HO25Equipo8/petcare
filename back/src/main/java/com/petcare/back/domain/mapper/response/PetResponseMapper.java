package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.PetResponseDTO;
import com.petcare.back.domain.entity.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetResponseMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.email", target = "ownerEmail")
    PetResponseDTO toDto(Pet pet);
}
