package com.petcare.back.domain.enumerated;

public enum VaccinationEnum {
    COMPLETO("Completo"),
    INCOMPLETO("Incompleto"),
    DESCONOCIDO("Desconocido");

    private final String label;

    VaccinationEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
