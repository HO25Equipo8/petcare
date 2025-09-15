package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.OfferingResponseDTO;
import com.petcare.back.domain.entity.Offering;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OfferingResponseMapper {
        @Mapping(target = "name", expression = "java(offering.getName().getLabel())")
        OfferingResponseDTO toDto(Offering offering);
}
