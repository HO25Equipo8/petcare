package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.request.UserPublicProfileDTO;
import com.petcare.back.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserFeedbackMapper {

    @Mapping(target = "location", expression = "java(user.getLocation() != null ? user.getLocation().getCity() : null)")
    @Mapping(target = "profilePhotoUrl", expression = "java(user.getProfilePhoto() != null ? \"/images/\" + user.getProfilePhoto().getId() : null)")
    @Mapping(target = "feedbackCount", expression = "java(user.getFeedbackReceived() != null ? user.getFeedbackReceived().size() : 0)")
    UserPublicProfileDTO toPublicProfile(User user);
}