package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.PetCreateDTO;
import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.response.BookingResponseDTO;
import com.petcare.back.domain.dto.response.ComboOfferingResponseDTO;
import com.petcare.back.domain.dto.response.PetResponseDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.BookingService;
import com.petcare.back.service.ComboOfferingService;
import com.petcare.back.service.PetService;
import com.petcare.back.service.PlanService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class OwnerController {

    private final PetService petService;
    private final ComboOfferingService comboOfferingService;
    private final PlanService planService;
    private final BookingService bookingService;

    @PostMapping("/register/pet")
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
    public ResponseEntity<List<ComboOfferingResponseDTO>> findAll() {
        return ResponseEntity.ok(comboOfferingService.findAll());
    }

    @PostMapping("/register/plan")
    public ResponseEntity<?> create(@RequestBody @Valid PlanCreateDTO dto, UriComponentsBuilder uriBuilder) {
        try {
            PlanResponseDTO plan = planService.createPlan(dto);

            URI uri = uriBuilder.path("/api/plans/{id}").buildAndExpand(plan.id()).toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Plan registrado con éxito",
                    "data", plan
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

    @PostMapping("/register/booking")
    public ResponseEntity<?> createBooking(
            @RequestBody @Valid BookingCreateDTO dto,
            UriComponentsBuilder uriBuilder
    ) {
        try {
            BookingResponseDTO booking = bookingService.createBooking(dto);

            URI uri = uriBuilder.path("/api/bookings/{id}")
                    .buildAndExpand(booking.id())
                    .toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Reserva registrada con éxito",
                    "data", booking
            ));
        } catch (RuntimeException e) {
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
}
