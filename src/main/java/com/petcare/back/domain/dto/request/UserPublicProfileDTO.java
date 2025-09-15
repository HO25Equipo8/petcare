package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;

import java.util.List;

public record UserPublicProfileDTO(
        String name,
        String location,
        String profilePhotoUrl,
        List<ProfessionalRoleEnum> professionalRoles,
        Integer feedbackCount
) {}

