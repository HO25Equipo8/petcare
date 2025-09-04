package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface BookingCreateMapper {
    @Mapping(target = "professionals", ignore = true)
    Booking toEntity(BookingCreateDTO dto);
}

