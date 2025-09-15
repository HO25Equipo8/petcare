package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.ComboOfferingResponseDTO;
import com.petcare.back.domain.entity.ComboOffering;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComboResponseMapper {
    @Mapping(target = "finalPrice", expression = "java(combo.getFinalPrice())")
    ComboOfferingResponseDTO toResponse(ComboOffering combo);
}
