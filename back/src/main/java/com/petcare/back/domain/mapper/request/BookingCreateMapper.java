package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingCreateMapper {

    @Mapping(target = "serviceItems", ignore = true)
    @Mapping(target = "schedules", ignore = true)
    Booking toEntity(BookingCreateDTO dto);
}
