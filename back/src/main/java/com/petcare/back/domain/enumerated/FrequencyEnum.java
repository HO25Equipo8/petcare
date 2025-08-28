package com.petcare.back.domain.enumerated;

public enum FrequencyEnum {
    DIARIO("Diario", 7.0),
    DOS_POR_SEMANA("2 veces por semana", 2.0),
    TRES_POR_SEMANA("3 veces por semana", 3.0),
    CUATRO_POR_SEMANA("4 veces por semana", 4.0),
    CADA_15_DIAS("Cada 15 días", 0.5),
    CADA_30_DIAS("Cada 30 días", 0.23),
    CADA_45_DIAS("Cada 45 días", 0.15);

    private final String label;
    private final double frequencyPerWeek;

    FrequencyEnum(String label, double frequencyPerWeek) {
        this.label = label;
        this.frequencyPerWeek = frequencyPerWeek;
    }

    public String getLabel() {
        return label;
    }

    public double getFrequencyPerWeek() {
        return frequencyPerWeek;
    }
}

