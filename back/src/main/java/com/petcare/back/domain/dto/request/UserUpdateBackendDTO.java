package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;
import java.util.List;

public record UserUpdateBackendDTO(
        String name,
        String phone,
        LocationDTO location,
        List<ProfessionalRoleEnum> professionalRoles
) {
}
