package com.petcare.back.infra.error;

import com.petcare.back.exception.MyException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity treatError404()
    {

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity treatError400(MethodArgumentNotValidException e)
    {
        var errors = e.getFieldErrors().stream().map(DataErrorValidation::new).toList();
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(IntegrityValidation.class)
    public ResponseEntity treatErrorIntegrityValidation(Exception e)
    {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity treatErrorBusinessValidation(Exception e)
    {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    private record DataErrorValidation(String field, String error){
        public DataErrorValidation(FieldError error)
        {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(Map.of(
                "status", "error",
                "message", "El archivo excede el tamaño máximo permitido. Intenta con una imagen más liviana."
        ));
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<?> handleMyException(MyException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "error",
                "message", "Error inesperado: " + ex.getClass().getSimpleName() + " - " + ex.getMessage()
        ));
    }
}
