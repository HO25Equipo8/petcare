package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.entity.ScheduleConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleConfigCreateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sitter", ignore = true) // lo seteás en el service
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ScheduleConfig toEntity(ScheduleConfigCreateDTO dto);
}