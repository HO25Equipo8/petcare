package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.*;
import com.petcare.back.domain.dto.response.*;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.OfferingVariantDescriptionEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/sitter")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class SitterController {

    private final ScheduleConfigService scheduleConfigService;
    private final OfferingService offeringService;
    private final ComboOfferingService comboOfferingService;
    private final PlanDiscountRuleService planDiscountRuleService;
    private final BookingService bookingService;

    @Operation(
            summary = "Registrar configuración de horarios",
            description = "Crea una nueva configuración de disponibilidad para el SITTER y genera los horarios correspondientes."
    )
    @PostMapping("/register/schedule")
    public ResponseEntity<?> create(@RequestBody @Valid ScheduleConfigCreateDTO dto, UriComponentsBuilder uriBuilder) {
        try {
            ScheduleConfigResponseDTO config = scheduleConfigService.createScheduleConfig(dto);

            URI uri = uriBuilder.path("/api/schedule-config/{id}").buildAndExpand(config.id()).toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Configuración registrada y horarios generados con éxito",
                    "data", config
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
            summary = "Consultar estado de configuración de horarios",
            description = "Devuelve el estado actual de la configuración de horarios del SITTER, incluyendo si está activa y cuántos turnos tiene."
    )
    @GetMapping("/schedule-config/status")
    public ResponseEntity<?> getScheduleStatus() {
        try {
            ScheduleConfigStatusResponseDTO status = scheduleConfigService.getScheduleStatus();
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", status
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "No se pudo obtener el estado de la configuración"
            ));
        }
    }

    @Operation(
            summary = "Registrar servicio",
            description = "Permite registrar un nuevo servicio individual ofrecido por el SITTER, como paseo, peluquería o veterinaria."
    )
    @PostMapping("/register/offering")
    public ResponseEntity<?> createOfering(@Valid @RequestBody OfferingCreateDTO dto, UriComponentsBuilder uriBuilder){
        try {
            OfferingResponseDTO service = offeringService.createService(dto);

            URI uri = uriBuilder.path("/offering/{id}").buildAndExpand(service.id()).toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Servicio registrado con éxito",
                    "data", service
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
            summary = "Registrar combo de servicios",
            description = "Permite registrar un combo que agrupa varios servicios en una sola oferta, con lógica de precios y variantes."
    )
    @PostMapping("/register/combo")
    public ResponseEntity<?> create(@RequestBody @Valid ComboOfferingCreateDTO dto, UriComponentsBuilder uriBuilder) {
        try {
            ComboOfferingResponseDTO combo = comboOfferingService.create(dto);

            URI uri = uriBuilder.path("/services/{id}").buildAndExpand(combo.id()).toUri();

            return ResponseEntity.created(uri).body(Map.of(
                    "status", "success",
                    "message", "Combo registrado con éxito",
                    "data", combo
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
            summary = "Registrar regla de descuento",
            description = "Crea una nueva regla de descuento aplicable a planes o servicios, según condiciones definidas."
    )
    @PostMapping("/register/discount/rule")
    public ResponseEntity<?> createRule(@RequestBody PlanDiscountRuleDTO rule) throws MyException {
        PlanDiscountRuleResponseDTO created = planDiscountRuleService.createRule(rule);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Regla de descuento creada con éxito",
                "data", created
        ));
    }

    @Operation(
            summary = "Listar reglas de descuento del profesional",
            description = "Devuelve las reglas de descuento creadas por el profesional autenticado, con sus condiciones y valores."
    )
    @GetMapping("/list/discount/rule")
    public ResponseEntity<?> getRulesForSitter() throws MyException {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", planDiscountRuleService.getRulesForSitter()
        ));
    }

    @Operation(
            summary = "Eliminar regla de descuento",
            description = "Elimina una regla de descuento existente según su ID. Solo accesible si no está en uso."
    )
    @DeleteMapping("/delete/discount/rule/{id}")
    public ResponseEntity<?> deleteRule(@PathVariable Long id) throws MyException {
        planDiscountRuleService.deleteRule(id);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Regla de descuento eliminada con éxito"
        ));
    }

    @Operation(
            summary = "Listar descripciones por tipo de servicio",
            description = "Devuelve las variantes descriptivas disponibles para cada tipo de servicio, agrupadas por categoría base."
    )
    @GetMapping("/list/variant/descriptions")
    public Map<OfferingEnum, List<String>> getDescriptionsByOffering() {
        return Arrays.stream(OfferingVariantDescriptionEnum.values())
                .collect(Collectors.groupingBy(
                        OfferingVariantDescriptionEnum::getBaseOffering,
                        Collectors.mapping(OfferingVariantDescriptionEnum::getDescription, Collectors.toList())
                ));
    }

    @Operation(
            summary = "Simular reserva",
            description = "Calcula una simulación de reserva con precios, descuentos y lógica de turnos, sin generar una reserva real."
    )
    @PostMapping("/simulation")
    public ResponseEntity<?> simulate(@RequestBody BookingSimulationRequestDTO dto) {
        BookingSimulationResponseDTO bookingSimulationResponseDTO = bookingService.simulateBooking(dto);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Simulacion creada con éxito",
                "data", bookingSimulationResponseDTO
        ));
    }

    @Operation(
            summary = "Confirmar reserva",
            description = "Permite al profesional confirmar una reserva pendiente o reprogramada"
    )
    @PutMapping("/booking/{id}/confirm")
    public ResponseEntity<?> confirmBooking(@PathVariable Long id) {
        User sitter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            BookingResponseDTO response = bookingService.confirmBooking(id, sitter);
            return ResponseEntity.ok(response);
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Cancelar reserva",
            description = "Permite al profesional cancelar una reserva activa. Libera los horarios asignados"
    )
    @PutMapping("/booking/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, @RequestBody String reason) {
        User sitter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            BookingResponseDTO response = bookingService.cancelBooking(id, sitter, reason);
            return ResponseEntity.ok(response);
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Proponer reprogramación",
            description = "Permite al profesional modificar los horarios de una reserva activa. El dueño deberá aceptar o rechazar la propuesta"
    )
    @PutMapping("/booking/{id}/reschedule")
    public ResponseEntity<?> rescheduleBooking(
            @PathVariable Long id,
            @RequestBody List<Long> newScheduleIds
    ) {
        User sitter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            BookingResponseDTO response = bookingService.rescheduleBooking(id, newScheduleIds, sitter);
            return ResponseEntity.ok(response);
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
