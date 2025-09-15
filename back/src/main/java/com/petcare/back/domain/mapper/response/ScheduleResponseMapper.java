package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.ScheduleResponseDTO;
import com.petcare.back.domain.entity.Schedule;
import com.petcare.back.domain.enumerated.WeekDayEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring")
public interface ScheduleResponseMapper {

    @Mapping(target = "id", source = "scheduleId")
    @Mapping(target = "time", source = "establishedTime")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "day", expression = "java(getDayOfWeek(schedule.getEstablishedTime()))")
    @Mapping(target = "configName", source = "scheduleConfig.configurationName")
    ScheduleResponseDTO toDto(Schedule schedule);

    default WeekDayEnum getDayOfWeek(Instant instant) {
        DayOfWeek dayOfWeek = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).getDayOfWeek();
        return WeekDayEnum.fromDayOfWeek(dayOfWeek);
    }
}
