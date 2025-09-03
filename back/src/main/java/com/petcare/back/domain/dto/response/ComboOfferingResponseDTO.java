package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.enumerated.ComboEnum;

import java.util.List;

public record ComboOfferingResponseDTO(
        Long id,
        ComboEnum name,
        String description,
        Double discount,
        Double finalPrice,
        List<OfferingCreateDTO> offerings,
        List<String> warnings

) {}
