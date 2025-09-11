package com.petcare.back.controller;

import com.petcare.back.domain.entity.Image;
import com.petcare.back.repository.ImageRepository;
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

    // Obtener una imagen por ID
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Image img = imageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(img.getImageType()))
                .body(img.getData());
    }
}
