package com.petcare.back.domain.enumerated;

public enum ServicePetsEnum {
    PASEO("Paseo"),
    BAÑO("Baño"),
    GUARDERIA("Guardería"),
    ADIESTRAMIENTO("Adiestramiento");

    private final String label;

    ServicePetsEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
