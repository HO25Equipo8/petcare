package com.petcare.back.domain.enumerated;

import java.util.Set;

public enum ComboEnum {

    PASEO_ASEO(Set.of(OfferingEnum.PASEO, OfferingEnum.ASEO), "Paseo + Aseo"),
    PASEO_GUARDERIA(Set.of(OfferingEnum.PASEO, OfferingEnum.GUARDERIA), "Paseo + Guardería"),
    VETERINARIA_ASEO(Set.of(OfferingEnum.VETERINARIA, OfferingEnum.ASEO), "Consulta + Aseo"),
    FULL_SERVICES(Set.of(OfferingEnum.VETERINARIA, OfferingEnum.ASEO, OfferingEnum.GUARDERIA, OfferingEnum.PASEO), "Consulta + Aseo + Guardería + Paseo"),
    ASEO_GUARDERIA(Set.of(OfferingEnum.ASEO, OfferingEnum.GUARDERIA), "Aseo + Guardería"),
    PASEO_VETERINARIA(Set.of(OfferingEnum.PASEO, OfferingEnum.VETERINARIA), "Paseo + Veterinaria"),
    GUARDERIA_VETERINARIA(Set.of(OfferingEnum.GUARDERIA, OfferingEnum.VETERINARIA), "Guardería + Veterinaria"),
    TRIPLE_CONFORT(Set.of(OfferingEnum.PASEO, OfferingEnum.ASEO, OfferingEnum.GUARDERIA), "Paseo + Aseo + Guardería"),
    SALUD_TOTAL(Set.of(OfferingEnum.VETERINARIA, OfferingEnum.ASEO, OfferingEnum.PASEO), "Veterinaria + Aseo + Paseo"),
    ACTIVO_Y_CUIDADO(Set.of(OfferingEnum.GUARDERIA, OfferingEnum.PASEO, OfferingEnum.VETERINARIA), "Guardería + Paseo + Veterinaria"),
    LIMPIO_Y_SANO(Set.of(OfferingEnum.ASEO, OfferingEnum.VETERINARIA), "Aseo + Veterinaria"),
    ULTRA_PACK(Set.of(OfferingEnum.PASEO, OfferingEnum.ASEO, OfferingEnum.GUARDERIA, OfferingEnum.VETERINARIA), "Paseo + Aseo + Guardería + Veterinaria"),
    CUIDADO_INTEGRAL(Set.of(OfferingEnum.GUARDERIA, OfferingEnum.ASEO, OfferingEnum.VETERINARIA), "Guardería + Aseo + Veterinaria"),
    BIENESTAR_TOTAL(Set.of(OfferingEnum.PASEO, OfferingEnum.ASEO, OfferingEnum.VETERINARIA), "Paseo + Aseo + Veterinaria");

    private final Set<OfferingEnum> expectedTypes;
    private final String label;

    ComboEnum(Set<OfferingEnum> expectedTypes, String label) {
        this.expectedTypes = expectedTypes;
        this.label = label;
    }

    public Set<OfferingEnum> getExpectedTypes() {
        return expectedTypes;
    }

    public String getLabel() {
        return label;
    }
}

