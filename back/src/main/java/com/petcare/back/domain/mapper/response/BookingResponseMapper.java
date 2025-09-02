package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.BookingResponseDTO;
import com.petcare.back.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingResponseMapper {

    @Mapping(target = "ownerName", expression = "java(booking.getOwner().getName())")
    @Mapping(target = "petName", expression = "java(booking.getPet().getName())")
    @Mapping(target = "offeringName",
            expression = "java(booking.getOffering() != null ? booking.getOffering().getName().name() : null)")
    @Mapping(target = "comboOfferingName",
            expression = "java(booking.getComboOffering() != null ? booking.getComboOffering().getName().name() : null)")
    @Mapping(target = "planName", expression = "java(booking.getPlan() != null ? booking.getPlan().getName() : null)")
    @Mapping(target = "scheduleDescription", expression =
            "java(booking.getSchedules().stream()" +
                    ".map(s -> s.getEstablishedTime() + \" - \" + s.getStatus())" +
                    ".toList())")
    @Mapping(target = "professionalNames", expression =
            "java(booking.getProfessionals().stream()" +
                    ".map(p -> p.getName() + \" - \" + p.getRole())" +
                    ".toList())"
    )
    BookingResponseDTO toDTO(Booking booking);
}


