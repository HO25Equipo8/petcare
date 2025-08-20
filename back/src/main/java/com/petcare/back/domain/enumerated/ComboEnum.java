package com.petcare.back.domain.enumerated;

public enum ComboEnum {
    PASEO_BAÑO("Paseo + Baño"),
    PASEO_GUARDERIA("Paseo + Guardería"),
    FULL_SERVICES("Todos los servicios");

    private final String label;

    ComboEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
