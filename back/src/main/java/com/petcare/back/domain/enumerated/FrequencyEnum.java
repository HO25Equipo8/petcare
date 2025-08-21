package com.petcare.back.domain.enumerated;

public enum FrequencyEnum {
    DIARIO("Diario"),
    QUINCENAL("Quincenal"),
    MENSUAL("Mensual"),
    TRIMESTRAL("Trimestral"),
    SEMESTRAL("Semestral"),
    ANUAL("Anual");

    private final String label;

    FrequencyEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
