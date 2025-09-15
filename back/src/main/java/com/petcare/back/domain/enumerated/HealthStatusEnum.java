package com.petcare.back.domain.enumerated;

public enum HealthStatusEnum {
    SANO("Sano"),
    TRATAMIENTO("En tratamiento"),
    ESPECIAL("Especial");

    private final String label;

    HealthStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
