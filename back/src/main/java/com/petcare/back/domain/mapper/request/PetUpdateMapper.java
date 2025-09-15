package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.PetUpdateDTO;
import com.petcare.back.domain.entity.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PetUpdateMapper {
    /**
     * Updates an existing Pet entity with data from PetUpdateDTO
     * Ignores null values to preserve existing data
     * Excludes non-updatable fields: id, createdAt, updatedAt, owner, active
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "age", ignore = true) // Will be calculated in service
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "imagePet", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(@MappingTarget Pet pet, PetUpdateDTO dto);
}
