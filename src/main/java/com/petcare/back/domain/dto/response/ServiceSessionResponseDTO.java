package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.ServiceSessionStatus;
import com.petcare.back.service.UpdateSessionService;

import java.time.LocalDateTime;
import java.util.List;

public record ServiceSessionResponseDTO(
        Long sessionId,
        Long bookingId,
        ServiceSessionStatus status,
        LocalDateTime startTime,
        List<UpdateServiceResponseDTO> updates
) { }
