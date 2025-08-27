package com.petcare.back.domain.enumerated;

public enum IncidentsTypes {

    MASCOTA_SE_SOLTO(IncidentsCategory.PASEO),
    MASCOTA_NO_QUIERE_CAMINAR(IncidentsCategory.PASEO),
    AGGRESION_A_OTRO_MASCOTA(IncidentsCategory.PASEO),

    MASCOTA_NO_QUIERE_BAÑARSE(IncidentsCategory.LAVADO),
    SHAMPOO_CAUSA_ALERGIA(IncidentsCategory.LAVADO),

    CORTE_ACCIDENTAL(IncidentsCategory.LAVADO),
    MASCOTA_INQUIETA(IncidentsCategory.LAVADO),
    MASCOTA_SE_ENFERMO_DURANTE_ESTADIA(IncidentsCategory.GUARDERIA),
    MASCOTA_SE_PELEO_CON_OTRA_MASCOTA(IncidentsCategory.GUARDERIA),
    DAÑOS_EN_LAS_INSTALACIONES(IncidentsCategory.GUARDERIA),
    MASCOTA_SE_LASTIMA_EN_EL_ENTRENAMIENTO(IncidentsCategory.ADIESTRAMIENTO),
    PROBLEMAS_DE_COMPORTAMIENTO(IncidentsCategory.ADIESTRAMIENTO),
    EL_DUEÑO_NO_RECOJIO_A_TIEMPO_LA_MASCOTA(IncidentsCategory.ADIESTRAMIENTO)

    ;
    private final IncidentsCategory incidentsCategory;

    IncidentsTypes(IncidentsCategory incidentsCategory) {
        this.incidentsCategory = incidentsCategory;
    }
public IncidentsCategory getIncidentsCategory() {
        return incidentsCategory;
}

}
