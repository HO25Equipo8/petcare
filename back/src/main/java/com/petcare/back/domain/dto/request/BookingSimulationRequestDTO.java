package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.CustomerCategory;

public record BookingSimulationRequestDTO(
        Long offeringId,
        Long comboOfferingId,
        CustomerCategory category
) {}

