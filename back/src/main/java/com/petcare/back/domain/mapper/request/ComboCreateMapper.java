package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.ComboServiceCreateDTO;
import com.petcare.back.domain.entity.ComboService;
import com.petcare.back.domain.entity.Service;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComboCreateMapper {
    ComboService toEntity(ComboServiceCreateDTO dto, List<Service> services);
}
