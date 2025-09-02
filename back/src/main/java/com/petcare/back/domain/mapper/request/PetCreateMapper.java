package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.PetCreateDTO;
import com.petcare.back.domain.entity.Pet;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring")
public interface PetCreateMapper {

    Pet toEntity(PetCreateDTO dto);

    PetCreateDTO toDto(Pet entity);

}