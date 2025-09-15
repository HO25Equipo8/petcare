package com.petcare.back.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BookingServiceItemCreateDTO(
        @NotNull(message = "Debés seleccionar un servicio")
        @Min(value = 1, message = "ID de servicio inválido")
        Long offeringId,

        @NotNull(message = "Debés seleccionar un horario")
        @Min(value = 1, message = "ID de horario inválido")
        Long scheduleId,

        @NotNull(message = "Debés seleccionar un profesional")
        @Min(value = 1, message = "ID de profesional inválido")
        Long professionalId
) {
}
