package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.ServiceResponseDTO;
import com.petcare.back.domain.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ServiceResponseMapper {
        @Mapping(target = "name", expression = "java(service.getName().getLabel())")
        ServiceResponseDTO toDto(Service service);
}
