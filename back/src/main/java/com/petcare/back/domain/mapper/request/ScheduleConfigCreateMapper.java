package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.entity.ScheduleConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleConfigCreateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sitter", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "turns", ignore = true)
        // lo seteás manualmente
    ScheduleConfig toEntity(ScheduleConfigCreateDTO dto);
}