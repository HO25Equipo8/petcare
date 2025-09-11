package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.UpdateServiceRequestDTO;
import com.petcare.back.domain.entity.UpdateService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateServiceCreateMapper {

    UpdateService toEntity(UpdateServiceRequestDTO dto);
}
