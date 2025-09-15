package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.LocationDTO;
import com.petcare.back.domain.entity.Location;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationCreateMapper {

    Location toEntity(LocationDTO dto);

    LocationDTO toDTO(Location location);
}
