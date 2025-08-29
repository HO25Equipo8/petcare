package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.entity.Booking;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface BookingCreateMapper {
    Booking toEntity(BookingCreateDTO dto);
}

