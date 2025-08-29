package com.petcare.back.domain.enumerated;

public enum PetSizeEnum {
    SMALL("Pequeño"),
    MEDIUM("Mediano"),
    LARGE("Grande"),
    EXTRA_LARGE("Muy grande");

    private final String label;

    PetSizeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
