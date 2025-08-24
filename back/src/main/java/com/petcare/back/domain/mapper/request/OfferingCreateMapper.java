package com.petcare.back.domain.mapper.request;


import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.entity.Offering;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OfferingCreateMapper {

    Offering toEntity(OfferingCreateDTO dto);

    OfferingCreateDTO toDo(Offering offering);
}

