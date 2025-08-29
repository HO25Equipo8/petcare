package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.entity.ComboOffering;
import com.petcare.back.domain.entity.Offering;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComboCreateMapper {
    ComboOffering toEntity(ComboOfferingCreateDTO dto, List<Offering> offerings);
}
