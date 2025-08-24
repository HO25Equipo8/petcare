package com.petcare.back.domain.dto.response;

import com.petcare.back.domain.dto.request.ServiceCreateDTO;
import com.petcare.back.domain.enumerated.ComboEnum;

import java.math.BigDecimal;
import java.util.List;

public record ComboServiceResponseDTO(
        Long id,
        ComboEnum name,
        String description,
        Double discount,
        Double finalPrice,
        List<ServiceCreateDTO> services
) {}
