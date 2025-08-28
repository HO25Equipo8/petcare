package com.petcare.back.domain.enumerated;

public enum ComboEnum {
    PASEO_BAÑO("Paseo + Baño"),
    PASEO_GUARDERIA("Paseo + Guardería"),
    VETERINARIA_ASEO("Consulta + Aseo"),
    FULL_SERVICES("Todos los servicios"),
    ASEO_GUARDERIA("Aseo + Guardería"),
    PASEO_VETERINARIA("Paseo + Veterinaria"),
    GUARDERIA_VETERINARIA("Guardería + Veterinaria"),
    TRIPLE_CONFORT("Paseo + Aseo + Guardería"),
    SALUD_TOTAL("Veterinaria + Aseo + Paseo"),
    ACTIVO_Y_CUIDADO("Guardería + Paseo + Veterinaria"),
    LIMPIO_Y_SANO("Aseo + Veterinaria"),
    ULTRA_PACK("Paseo + Aseo + Guardería + Veterinaria"),
    CUIDADO_INTEGRAL("Guardería + Aseo + Veterinaria"),
    BIENESTAR_TOTAL("Paseo + Aseo + Veterinaria");

    private final String label;

    ComboEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
