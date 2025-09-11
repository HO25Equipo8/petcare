package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.UpdateServiceResponseDTO;
import com.petcare.back.domain.entity.Image;
import com.petcare.back.domain.entity.UpdateService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UpdateServiceResponseMapper {

    @Mapping(target = "imageUrl", expression = "java(entity.getImage() != null ? \"/api/images/\" + entity.getImage().getId() : null)")
    UpdateServiceResponseDTO toDto(UpdateService entity);
}

