package com.petcare.back.domain.enumerated;

public enum TemperamentEnum {
    TRANQUILO("Tranquilo"),
    JUGUETON("Juguetón"),
    AGRESIVO("Agresivo"),
    ANSIOSO("Ansioso");

    private final String label;

    TemperamentEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}