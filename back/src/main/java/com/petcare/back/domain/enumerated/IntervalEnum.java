package com.petcare.back.domain.enumerated;

public enum IntervalEnum {
    SEMANAL("Semanal"),
    QUINCENAL("Quincenal"),
    MENSUAL("Mensual"),
    TRIMESTRAL("Trimestral"),
    SEMESTRAL("Semestral"),
    ANUAL("Anual");

    private final String label;

    IntervalEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
