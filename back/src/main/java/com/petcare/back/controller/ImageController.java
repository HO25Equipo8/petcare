package com.petcare.back.controller;

import com.petcare.back.domain.entity.Image;
import com.petcare.back.repository.ImageRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://127.0.0.1:5501", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageRepository imageRepository;

    @Operation(
            summary = "Obtener imagen por ID",
            description = "Devuelve el archivo de imagen correspondiente al ID especificado. " +
                    "La respuesta incluye el contenido binario de la imagen con su tipo MIME original. " +
                    "Este endpoint es útil para mostrar imágenes almacenadas en la base de datos directamente en el frontend."
    )
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image img = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(img.getImageType()))
                .body(img.getData());
    }
}
