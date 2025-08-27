package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.ProfessionalRoleEnum;

import java.math.BigDecimal;
import java.util.List;

public record BookingCreateDTO(
        Long petId,
        Long offeringId,
        Long comboOfferingId,
        Long planId,
        List<Long> scheduleIds,
        BigDecimal finalPrice,
        List<ProfessionalBookingDTO> professionals
) {
    public record ProfessionalBookingDTO(
            Long userId,
            ProfessionalRoleEnum roleProfessional
    ) {}
}
