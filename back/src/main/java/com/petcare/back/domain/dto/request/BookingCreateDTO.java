package com.petcare.back.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookingCreateDTO(
        @NotNull(message = "Debés seleccionar una mascota")
        @Min(value = 1, message = "ID de mascota inválido")
        Long petId,

        // Puede ser null, pero si se envia, debe ser >= 1
        @Min(value = 1, message = "ID de servicio inválido")
        Long offeringId,

        // Puede ser null, pero si se envia, debe ser >= 1
        @Min(value = 1, message = "ID de combo inválido")
        Long comboOfferingId,

        // Puede ser null, pero si se envia, debe ser >= 1
        @Min(value = 1, message = "ID de plan inválido")
        Long planId,

        @NotEmpty(message = "Debés seleccionar al menos un horario")
        List<Long> scheduleIds,

        @NotEmpty(message = "Debés seleccionar al menos un profesional")
        List<Long> professionals
) {
}
