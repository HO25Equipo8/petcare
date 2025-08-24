package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.PetCreateDTO;
import com.petcare.back.domain.dto.response.ComboServiceResponseDTO;
import com.petcare.back.domain.dto.response.PetResponseDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.ComboServiceService;
import com.petcare.back.service.PetService;
import com.petcare.back.service.PlanService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class OwnerController {

    private final PetService petService;
    private final ComboServiceService comboServiceService;
    private final PlanService planService;

    @PostMapping("/pet/register")
    public ResponseEntity<?> registerPet(@Valid @RequestBody PetCreateDTO petCreateDTO,
                                         UriComponentsBuilder uriBuilder) {
        try {
            PetResponseDTO pet = petService.createPet(petCreateDTO);

            URI uri = uriBuilder.path("/pets/{id}").buildAndExpand(pet.id()).toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Mascota registrada con éxito",
                    "data", pet
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error interno del servidor"
            ));
        }
    }

    @GetMapping("/list/combo")
    public ResponseEntity<List<ComboServiceResponseDTO>> findAll() {
        return ResponseEntity.ok(comboServiceService.findAll());
    }
    @GetMapping("/list/plan")
    public ResponseEntity<?> getAll() {
        try {
            List<PlanResponseDTO> plans = planService.getAllPlans();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Planes obtenidos con éxito",
                    "data", plans
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al obtener los planes"
            ));
        }
    }
}
