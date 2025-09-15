package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;

import java.util.List;

public record UserUpdateDTO(
        String name,
        String phone,
        AutocompleteSuggestion location,
        List<ProfessionalRoleEnum> professionalRoles
) {}
