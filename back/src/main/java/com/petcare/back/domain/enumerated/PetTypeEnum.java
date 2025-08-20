package com.petcare.back.domain.enumerated;

public enum PetTypeEnum {
    PERRO("Perro"),
    GATO("Gato"),
    OTRO("Otro");

    private final String label;

    PetTypeEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
