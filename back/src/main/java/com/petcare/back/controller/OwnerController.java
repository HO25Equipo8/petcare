package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.PetCreateDTO;
import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.response.*;
import com.petcare.back.domain.entity.User;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserService userService;

    @Operation(
            summary = "Registrar mascota",
            description = "Permite registrar una nueva mascota asociada al usuario autenticado, incluyendo datos como nombre, edad, tipo y necesidades especiales."
    )
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

    @Operation(
            summary = "Listar combos de servicios",
            description = "Devuelve todos los combos registrados en el sistema, cada uno compuesto por múltiples servicios agrupados."
    )
    @GetMapping("/list/combo")
    public ResponseEntity<List<ComboOfferingResponseDTO>> findAll() {
        return ResponseEntity.ok(comboOfferingService.findAll());
    }

    @Operation(
            summary = "Seleccionar plan como dueño",
            description = "Permite al usuario con rol OWNER elegir un plan de servicios según frecuencia e intervalo. El sistema genera automáticamente el nombre del plan, calcula la cantidad de sesiones semanales y aplica el descuento correspondiente según las reglas configuradas por el profesional."
    )
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

    @Operation(
            summary = "Consultar plan actual del usuario",
            description = "Devuelve el plan de servicios actualmente asignado al usuario autenticado. Incluye frecuencia, intervalo y porcentaje de descuento aplicado. Solo disponible para usuarios con rol OWNER."
    )
    @GetMapping("/my-plan")
    public ResponseEntity<?> getMyPlan() {
        try {
            User user = getAuthenticatedUser();
            PlanResponseDTO plan = planService.getPlanByUser(user.getId());

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Plan del usuario obtenido con éxito",
                    "data", plan
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al obtener el plan del usuario"
            ));
        }
    }

    @Operation(
            summary = "Registrar reserva",
            description = "Permite al usuario OWNER crear una nueva reserva para una mascota, seleccionando horarios, profesionales y servicios. Si el usuario tiene un plan activo, se aplica automáticamente el descuento correspondiente en el cálculo del precio final."
    )
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

    @Operation(
            summary = "Buscar profesionales cercanos",
            description = "Devuelve una lista de SITTERs activos dentro del radio especificado en kilómetros, tomando como referencia la ubicación del usuario autenticado."
    )
    @PostMapping("/search/nearby-sitters")
    public ResponseEntity<?> searchNearbySitters(@RequestParam double radiusKm,
                                                 UriComponentsBuilder uriBuilder) {
        try {
            List<NearbySitterResponseDTO> sitters = userService.findNearbySitters(radiusKm);

            URI uri = uriBuilder.path("/sitters/search").build().toUri();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Profesionales encontrados dentro de " + radiusKm + " km",
                    "data", sitters,
                    "searchUri", uri.toString()
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

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
