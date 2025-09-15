package com.petcare.back.domain.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
@JsonFormat(shape = JsonFormat.Shape.STRING)
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

    @JsonCreator
    public static PetTypeEnum from(String value) {
        for (PetTypeEnum type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de mascota inválido: " + value);
    }
}
