package com.petcare.back.domain.enumerated;

import java.util.Arrays;

public enum ServicePetsEnum {
    PASEO("Paseo"),
    ASEO("Aseo"),
    GUARDERIA("Guardería"),
    VETERINARIA("Veterinaria");

    private final String label;

    ServicePetsEnum(String label) { this.label = label; }

    public String getLabel() { return label; }

    public static ServicePetsEnum fromLabel(String label) {
        return Arrays.stream(values())
                .filter(e -> e.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Servicio no válido: " + label));
    }
}