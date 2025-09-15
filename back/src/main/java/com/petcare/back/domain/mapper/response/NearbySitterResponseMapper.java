package com.petcare.back.domain.mapper.response;

import com.petcare.back.domain.dto.response.NearbySitterResponseDTO;
import com.petcare.back.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NearbySitterResponseMapper {

    @Mapping(target = "photoUrl", expression = "java(user.getProfilePhoto() != null ? \"/images/\" + user.getProfilePhoto().getId() : null)")
    @Mapping(target = "city", expression = "java(user.getLocation() != null ? user.getLocation().getCity() : null)")
    @Mapping(target = "latitude", expression = "java(user.getLocation() != null ? user.getLocation().getLatitude() : null)")
    @Mapping(target = "longitude", expression = "java(user.getLocation() != null ? user.getLocation().getLongitude() : null)")
    @Mapping(target = "averageRating", expression = "java(user.getFeedbackReceived() != null && !user.getFeedbackReceived().isEmpty() ? user.getFeedbackReceived().stream().mapToInt(f -> f.getRating()).average().orElse(0.0) : null)")
    @Mapping(target = "feedbackCount", expression = "java(user.getFeedbackReceived() != null ? user.getFeedbackReceived().size() : 0)")
    NearbySitterResponseDTO toDto(User user);
}
