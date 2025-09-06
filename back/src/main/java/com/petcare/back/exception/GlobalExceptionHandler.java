package com.petcare.back.exception;

import com.petcare.back.validation.ValidateOfferingRoleCompatibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ValidateOfferingRoleCompatibility.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseError(HttpMessageNotReadableException ex) {
        String detalle = ex.getMessage();

        if (detalle.contains("Tipo de mascota inválido")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Tipo de mascota inválido. Usá uno de: PERRO, GATO, OTRO.",
                    "status", "error"
            ));
        }

        if (detalle.contains("OfferingEnum")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Nombre de servicio inválido. Usá uno de: PASEO, ASEO, GUARDERIA, VETERINARIA.",
                    "status", "error"
            ));
        }

        return ResponseEntity.badRequest().body(Map.of(
                "message", "Error al interpretar el JSON. Revisá los campos y asegurate de que los valores sean válidos.",
                "status", "error"
        ));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        StringBuilder errores = new StringBuilder("Errores de validación:\n");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.append("- ").append(error.getField()).append(": ").append(error.getDefaultMessage()).append("\n")
        );
        return ResponseEntity.badRequest().body(errores.toString());
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<Map<String, Object>> handleMyException(MyException ex) {
        log.warn("MyException: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "error",
                "message", "Error interno del servidor"
        ));
    }
}

