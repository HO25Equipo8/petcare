package com.petcare.back.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
    public ResponseEntity<String> handleCustomErrors(MyException ex) {
        return ResponseEntity.badRequest().body("Error de negocio: " + ex.getMessage());
    }
}

