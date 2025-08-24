package com.petcare.back.domain.mapper.request;


import com.petcare.back.domain.dto.request.ServiceCreateDTO;
import com.petcare.back.domain.dto.response.ServiceResponseDTO;
import com.petcare.back.domain.entity.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ServiceCreateMapper {

    Service toEntity(ServiceCreateDTO dto);

    ServiceCreateDTO toDo(Service service);
}

