package com.petcare.back.domain.enumerated;

public enum FeedbackContext {

    BOOKING("Reserva completada", "⭐ Opinión tras una reserva exitosa"),
    INCIDENT("Resolución de incidente", "🛠 Comentario tras resolver un problema"),
    POST_INTERACTION("Interacción sin reserva", "💬 Feedback tras una charla o contacto informal"),
    OTHER("Otro", "📌 Comentario general sin contexto específico"),
    NEGATIVE_EXPERIENCE("Experiencia negativa", "⚠️ Reporte de una situación insatisfactoria"),
    POSITIVE_SURPRISE("Sorpresa positiva", "🎉 Reconocimiento por superar expectativas"),
    RECOMMENDATION("Recomendación", "👍 Recomendación directa para otros usuarios");

    private final String label;
    private final String description;

    FeedbackContext(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}

