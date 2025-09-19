package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import java.util.List;

public record NearbySitterResponseDTO(
        Long id,
        String name,
        String photoUrl,
        String city,
        Double latitude,
        Double longitude,
        List<ProfessionalRoleEnum> roles,
        Double averageRating,
        Integer feedbackCount
) {
}
