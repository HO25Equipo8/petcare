package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.UserUpdateResponseDTO;
import com.petcare.back.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface UserUpdateResponseMapper {
    @Mapping(target = "location", source = "location")
    UserUpdateResponseDTO toDTO(User user);
}
