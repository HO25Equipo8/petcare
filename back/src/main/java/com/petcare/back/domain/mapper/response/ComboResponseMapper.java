package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.ComboServiceResponseDTO;
import com.petcare.back.domain.entity.ComboService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComboResponseMapper {
    @Mapping(target = "finalPrice", expression = "java(combo.getFinalPrice())")
    ComboServiceResponseDTO toResponse(ComboService combo);
}
