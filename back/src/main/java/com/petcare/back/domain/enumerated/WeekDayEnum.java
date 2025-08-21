package com.petcare.back.domain.enumerated;

public enum WeekDayEnum {
    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sábado"),
    DOMINGO("Domingo");

    private final String label;

    WeekDayEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
