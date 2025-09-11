package com.petcare.back.domain.dto.request;

public record AutocompleteSuggestion(
        String description,
        String placeId
) {
}
