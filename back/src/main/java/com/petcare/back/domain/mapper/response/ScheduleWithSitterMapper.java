package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.ScheduleWithSitterDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.enumerated.WeekDayEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface ScheduleWithSitterMapper {

    @Mapping(target = "scheduleId", source = "scheduleId")
    @Mapping(target = "time", source = "establishedTime")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "day", expression = "java(mapDay(schedule.getEstablishedTime()))")
    @Mapping(target = "configName", source = "scheduleConfig.configurationName")
    @Mapping(target = "sitterId", source = "scheduleConfig.sitter.id")
    @Mapping(target = "sitterName", source = "scheduleConfig.sitter.name")
    @Mapping(target = "sitterEmail", source = "scheduleConfig.sitter.email")
    ScheduleWithSitterDTO toDto(Schedule schedule);

    // 🔧 Método auxiliar para resolver el día
    default String mapDay(Instant establishedTime) {
        return WeekDayEnum.fromDayOfWeek(
                LocalDateTime.ofInstant(establishedTime, ZoneId.systemDefault()).getDayOfWeek()
        ).getLabel();
    }
}
