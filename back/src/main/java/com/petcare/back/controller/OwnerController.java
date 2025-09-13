package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.BookingCreateDTO;
import com.petcare.back.domain.dto.request.PetCreateDTO;
import com.petcare.back.domain.dto.request.PetUpdateDTO;
import com.petcare.back.domain.dto.response.*;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.PlanType;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
            summary = "Actualizar mascota",
            description = "Permite actualizar los datos de una mascota existente. Solo el propietario de la mascota o un administrador pueden realizar esta acción. Los campos no proporcionados mantendrán sus valores actuales."
    )
    @PutMapping("/update/pet/{petId}")
    public ResponseEntity<?> updatePet(@PathVariable Long petId,
                                       @Valid @RequestBody PetUpdateDTO petUpdateDTO) {
        try {
            PetResponseDTO updatedPet = petService.updatePet(petId, petUpdateDTO);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Mascota actualizada con éxito",
                    "data", updatedPet
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
            summary = "Eliminar mascota (lógicamente)",
            description = "Permite eliminar lógicamente una mascota estableciendo su estado como inactivo. " +
                    "Solo el propietario de la mascota o un administrador pueden realizar esta acción."
    )
    @DeleteMapping("/delete/pet/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId) {
        try {
            petService.deletePet(petId);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Mascota dada de baja con éxito"
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
            summary = "Suscribirse a un plan",
            description = "Permite al usuario con rol OWNER elegir un plan de suscripción disponible en la plataforma. El plan determina el acceso a funcionalidades como actualizaciones en vivo y tracking."
    )
    @PutMapping("/subscribe/plan")
    public ResponseEntity<?> subscribe(@RequestParam PlanType type) {
        try {
            PlanResponseDTO plan = planService.subscribeToPlan(type);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Suscripción actualizada a " + type.name(),
                    "data", plan
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @Operation(
            summary = "Consultar plan actual del usuario",
            description = "Devuelve el plan de suscripción actualmente asignado al usuario autenticado. Incluye tipo de plan, precio y funcionalidades habilitadas."
    )
    @GetMapping("/my-plan")
    public ResponseEntity<?> getMyPlan() {
        try {
            PlanResponseDTO plan = planService.getMyPlan();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Plan actual obtenido con éxito",
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

    @Operation(
            summary = "Responder propuesta de reprogramación de un servicio",
            description = "Permite al dueño aceptar o rechazar la reprogramación propuesta por el profesional para un ítem específico"
    )
    @PutMapping("/booking/item/{itemId}/respond-reprogram")
    public ResponseEntity<?> respondToReprogramItem(
            @PathVariable Long itemId,
            @RequestParam boolean accept
    ) {
        User owner = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            bookingService.respondToItemReschedule(itemId, accept, owner);
            return ResponseEntity.ok(Map.of("status", "actualizado"));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
