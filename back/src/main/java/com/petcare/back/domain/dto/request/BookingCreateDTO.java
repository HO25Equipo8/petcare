package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.entity.User;

import java.util.List;

public record BookingCreateDTO(
        Long petId,
        Long offeringId,
        Long comboOfferingId,
        Long planId,
        List<Long> scheduleIds,
        List<User> professionals
) {
}
