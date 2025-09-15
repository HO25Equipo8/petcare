package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.ScheduleTurnResponseDTO;
import com.petcare.back.domain.entity.ScheduleTurn;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScheduleTurnResponseMapper {

    ScheduleTurnResponseDTO toDto(ScheduleTurn entity);

    List<ScheduleTurnResponseDTO> toDtoList(List<ScheduleTurn> entities);
}
