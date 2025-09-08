package com.petcare.back.domain.mapper.request;

import com.petcare.back.domain.dto.response.FeedbackResponseDTO;
import com.petcare.back.domain.entity.Feedback;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "contextLabel", source = "context.label")
    @Mapping(target = "contextDescription", source = "context.description")
    FeedbackResponseDTO toDto(Feedback feedback);
    List<FeedbackResponseDTO> toDtoList(List<Feedback> feedbacks);
}
