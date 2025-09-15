package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.ServiceItemResponseDTO;
import com.petcare.back.domain.entity.BookingServiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceItemMapper {
    @Mapping(target = "offeringName", expression = "java(item.getOffering().getName().name())")
    @Mapping(target = "professionalName", expression = "java(item.getProfessional().getName())")
    @Mapping(target = "professionalRole", expression = "java(item.getProfessional().getRole().name())")
    @Mapping(target = "scheduleDescription", expression =
            "java(item.getSchedule().getEstablishedTime() + \" - \" + item.getSchedule().getStatus().name())")
    ServiceItemResponseDTO toDTO(BookingServiceItem item);
}

