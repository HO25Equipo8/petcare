package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.enumerated.PetTypeEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ServiceResponseDTO(Long id,
                                 String name,
                                 String description,
                                 BigDecimal basePrice,
                                 List<PetTypeEnum> applicablePetTypes,
                                 LocalDateTime createdAt) {
}
