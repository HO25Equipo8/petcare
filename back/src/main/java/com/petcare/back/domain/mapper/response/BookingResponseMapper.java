package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.BookingResponseDTO;
import com.petcare.back.domain.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ServiceItemMapper.class)
public interface BookingResponseMapper {

    @Mapping(target = "ownerName", expression = "java(booking.getOwner().getName())")
    @Mapping(target = "petName", expression = "java(booking.getPet().getName())")
    @Mapping(target = "comboOfferingName", expression =
            "java(booking.getComboOffering() != null ? booking.getComboOffering().getName().name() : null)")
    @Mapping(target = "items", source = "serviceItems") // ✅ MapStruct usa ServiceItemMapper
    BookingResponseDTO toDTO(Booking booking);
}
