package com.petcare.back.domain.enumerated;

import java.time.DayOfWeek;

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

    public static WeekDayEnum fromDayOfWeek(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> LUNES;
            case TUESDAY -> MARTES;
            case WEDNESDAY -> MIERCOLES;
            case THURSDAY -> JUEVES;
            case FRIDAY -> VIERNES;
            case SATURDAY -> SABADO;
            case SUNDAY -> DOMINGO;
        };
    }
}
