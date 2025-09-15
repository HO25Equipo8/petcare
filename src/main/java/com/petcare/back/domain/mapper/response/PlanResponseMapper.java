package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.domain.entity.Plan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlanResponseMapper {
    PlanResponseDTO toDto(Plan plan);
}
