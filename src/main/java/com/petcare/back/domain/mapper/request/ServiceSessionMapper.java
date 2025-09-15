package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.response.ServiceSessionResponseDTO;
import com.petcare.back.domain.entity.ServiceSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceSessionMapper {

    ServiceSessionMapper INSTANCE = Mappers.getMapper(ServiceSessionMapper.class);

    @Mapping(source = "id", target = "sessionId")
    ServiceSessionResponseDTO toDto(ServiceSession session);
}

