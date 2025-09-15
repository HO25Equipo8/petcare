package com.petcare.back.domain.dto.request;

import com.petcare.back.domain.enumerated.ComboEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ComboOfferingCreateDTO(
        @NotNull ComboEnum name,
        @NotBlank
        @Schema(
                description = "Descripción general del combo. Ejemplo: 'Combo ideal para mascotas activas que incluye paseo y baño.'",
                example = "Combo paseo + baño para mascotas energéticas"
        )
        String description,
        @NotNull @DecimalMin("0.0") Double discount,
        @NotEmpty(message = "Debe incluir al menos un servicio en el combo")
        List<Long> offeringIds
) {
}
