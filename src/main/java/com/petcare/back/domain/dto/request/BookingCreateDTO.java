package com.petcare.back.domain.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BookingCreateDTO(
        @NotNull(message = "Debés seleccionar una mascota")
        @Min(value = 1, message = "ID de mascota inválido")
        Long petId,

        @Min(value = 1, message = "ID de combo inválido")
        Long comboOfferingId,

        @NotEmpty(message = "Debés enviar al menos un ítem de servicio")
        List<BookingServiceItemCreateDTO> items
) {
}
