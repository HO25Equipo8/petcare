package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.ScheduleConfigResponseDTO;
import com.petcare.back.domain.entity.ScheduleConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleConfigResponseMapper {

        @Mapping(target = "sitterName", source = "sitter.name")
        @Mapping(target = "totalSchedulesGenerated", ignore = true)
        ScheduleConfigResponseDTO toDto(ScheduleConfig config);
}
