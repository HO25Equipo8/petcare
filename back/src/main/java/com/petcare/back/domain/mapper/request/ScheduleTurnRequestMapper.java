package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.ScheduleTurnCreateDTO;
import com.petcare.back.domain.entity.ScheduleTurn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleTurnRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scheduleConfig", ignore = true) // lo seteás en el service
    ScheduleTurn toEntity(ScheduleTurnCreateDTO dto);
}
