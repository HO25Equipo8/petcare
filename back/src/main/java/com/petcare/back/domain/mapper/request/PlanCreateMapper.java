package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.entity.Plan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlanCreateMapper {
    Plan toEntity(PlanCreateDTO dto);
}
