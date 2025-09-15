package com.petcare.back.domain.enumerated;

public enum BookingStatusEnum {
    PENDIENTE("Pendiente"),
    PENDIENTE_REPROGRAMAR("Pendiente de reprogamar"),
    CONFIRMADO("Confirmado"),
    REPROGRAMAR("Reprogramar"),
    CANCELADO("Cancelado"),
    COMPLETADO("Completado");

    private final String label;

    BookingStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
