package com.petcare.back.domain.enumerated;

public enum OfferingVariantDescriptionEnum {

    // 🧼 ASEO
    ASEO_CORTE("ASEO", "Baño completo con corte", OfferingVariantScheduleTypeEnum.DIURNO),
    ASEO_SIN_CORTE("ASEO", "Solo baño sin corte", OfferingVariantScheduleTypeEnum.DIURNO),
    ASEO_EXPRESS("ASEO", "Baño express", OfferingVariantScheduleTypeEnum.FLEXIBLE),
    ASEO_HIPOALERGENICO("ASEO", "Baño hipoalergénico", OfferingVariantScheduleTypeEnum.DIURNO),
    ASEO_CON_PERFUME("ASEO", "Baño con perfume y secado", OfferingVariantScheduleTypeEnum.DIURNO),
    ASEO_DESPARASITANTE("ASEO", "Baño con tratamiento antipulgas", OfferingVariantScheduleTypeEnum.FLEXIBLE),

    // 🐾 PASEO
    PASEO_DIURNO("PASEO", "Paseo matutino", OfferingVariantScheduleTypeEnum.DIURNO),
    PASEO_NOCTURNO("PASEO", "Paseo nocturno", OfferingVariantScheduleTypeEnum.NOCTURNO),
    PASEO_INTENSIVO("PASEO", "Paseo largo con juego", OfferingVariantScheduleTypeEnum.DIURNO),
    PASEO_SOCIAL("PASEO", "Paseo grupal con otros perros", OfferingVariantScheduleTypeEnum.DIURNO),
    PASEO_INDIVIDUAL("PASEO", "Paseo personalizado", OfferingVariantScheduleTypeEnum.FLEXIBLE),
    PASEO_ENTRENAMIENTO("PASEO", "Paseo con refuerzo de conducta", OfferingVariantScheduleTypeEnum.DIURNO),

    // 🏠 GUARDERIA
    GUARDERIA_DIURNA("GUARDERIA", "Guardería diurna", OfferingVariantScheduleTypeEnum.DIURNO),
    GUARDERIA_NOCTURNA("GUARDERIA", "Guardería nocturna", OfferingVariantScheduleTypeEnum.NOCTURNO),
    GUARDERIA_FINDE("GUARDERIA", "Guardería de fin de semana", OfferingVariantScheduleTypeEnum.FLEXIBLE),
    GUARDERIA_LARGA_ESTANCIA("GUARDERIA", "Guardería por varios días", OfferingVariantScheduleTypeEnum.FLEXIBLE),
    GUARDERIA_CON_JUEGOS("GUARDERIA", "Guardería con actividades", OfferingVariantScheduleTypeEnum.DIURNO),
    GUARDERIA_TRANQUILA("GUARDERIA", "Guardería para mascotas mayores", OfferingVariantScheduleTypeEnum.FLEXIBLE),

    // 🩺 VETERINARIA
    VETERINARIA_CONTROL("VETERINARIA", "Consulta general", OfferingVariantScheduleTypeEnum.DIURNO),
    VETERINARIA_VACUNACION("VETERINARIA", "Vacunación", OfferingVariantScheduleTypeEnum.DIURNO),
    VETERINARIA_DESPARASITACION("VETERINARIA", "Desparasitación", OfferingVariantScheduleTypeEnum.DIURNO),
    VETERINARIA_URGENCIA("VETERINARIA", "Atención de urgencia", OfferingVariantScheduleTypeEnum.FLEXIBLE),
    VETERINARIA_NUTRICION("VETERINARIA", "Consulta nutricional", OfferingVariantScheduleTypeEnum.DIURNO),
    VETERINARIA_SEGUIMIENTO("VETERINARIA", "Seguimiento post tratamiento", OfferingVariantScheduleTypeEnum.DIURNO);

    private final OfferingEnum baseOffering;
    private final String description;
    private final OfferingVariantScheduleTypeEnum scheduleType;

    OfferingVariantDescriptionEnum(String baseOffering, String description, OfferingVariantScheduleTypeEnum scheduleType) {
        this.baseOffering = OfferingEnum.valueOf(baseOffering);
        this.description = description;
        this.scheduleType = scheduleType;
    }

    public OfferingEnum getBaseOffering() {
        return baseOffering;
    }

    public String getDescription() {
        return description;
    }

    public OfferingVariantScheduleTypeEnum getScheduleType() {
        return scheduleType;
    }
}
