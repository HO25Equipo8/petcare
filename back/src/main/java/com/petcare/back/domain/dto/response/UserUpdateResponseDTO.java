package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.dto.request.LocationDTO;
import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import java.util.List;

public record UserUpdateResponseDTO(
        Long id,
        String name,
        String phone,
        LocationDTO location,
        List<ProfessionalRoleEnum> professionalRoles
) {
}
