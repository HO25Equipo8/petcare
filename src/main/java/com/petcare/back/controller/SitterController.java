package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.*;
import com.petcare.back.domain.dto.response.*;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.entity.ScheduleConfig;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.OfferingVariantDescriptionEnum;
import com.petcare.back.domain.enumerated.ScheduleStatus;
import com.petcare.back.domain.enumerated.WeekDayEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.time.LocalDate;
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
            summary = "Listar todas las configuraciones horarias propias",
            description = "Devuelve todas las configuraciones horarias registradas por el profesional autenticado, incluyendo turnos y cantidad de horarios generados."
    )
    @GetMapping("/schedule/config/all")
    public ResponseEntity<?> getAllScheduleConfigs() {
        try {
            List<ScheduleConfigResponseDTO> configs = scheduleConfigService.getAllConfigsBySitter();

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", configs
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
            summary = "Eliminar configuración horaria",
            description = "Desactiva la configuración horaria actual del profesional, eliminando los horarios futuros no reservados."
    )
    @DeleteMapping("/schedule/config/{id}/desactivate")
    public ResponseEntity<?> deleteScheduleConfig(@PathVariable Long id, @AuthenticationPrincipal User sitter) {
        try {
            scheduleConfigService.deactivateConfig(id, sitter);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Configuración horaria eliminada correctamente"
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
    @Operation(
            summary = "Activar configuración horaria",
            description = "Activa una configuración horaria previamente desactivada y regenera los horarios futuros según sus turnos."
    )
    @PutMapping("/schedule/config/{id}/activate")
    public ResponseEntity<?> activateScheduleConfig(@PathVariable Long id, @AuthenticationPrincipal User sitter) {
        try {
            ScheduleConfigResponseDTO config = scheduleConfigService.activateConfig(id, sitter);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Configuración activada y horarios generados con éxito",
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

    @PutMapping("/schedule/{id}/reschedule")
    public ResponseEntity<?> reschedule(@PathVariable Long id,
                                        @RequestBody @Valid ScheduleRescheduleDTO dto,
                                        @AuthenticationPrincipal User sitter) {
        try {
            ScheduleResponseDTO updated = scheduleConfigService.reschedule(id, dto, sitter);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Horario reprogramado con éxito",
                    "data", updated
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @GetMapping("/schedule/config/visual")
    public ResponseEntity<?> getVisualConfigs() {
        try {
            List<Map<String, Object>> visualBlocks = scheduleConfigService.getVisualBlocks();
            return ResponseEntity.ok(Map.of("status", "success", "data", visualBlocks));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("status", "error", "message", "Error interno del servidor"));
        }
    }

    @PostMapping("/schedule/config/{id}/duplicate")
    public ResponseEntity<?> duplicateConfig(@PathVariable Long id,
                                             @AuthenticationPrincipal User sitter) {
        try {
            ScheduleConfigResponseDTO duplicated = scheduleConfigService.duplicateConfig(id, sitter);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Configuración duplicada con éxito",
                    "data", duplicated
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @Operation(
            summary = "Registrar servicio",
            description = """
        Permite registrar un nuevo servicio individual ofrecido por el SITTER, como paseo, peluquería o veterinaria.
        El campo 'description' debe coincidir exactamente con una de las variantes válidas según el tipo de servicio.
        Las variantes disponibles pueden consultarse en el endpoint /sitter/list/variant/descriptions.
        """
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
            summary = "Listar servicios propios",
            description = "Devuelve todos los servicios individuales registrados por el profesional autenticado."
    )
    @GetMapping("/my/offering")
    public ResponseEntity<?> getMyOfferings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User sitter = (User) authentication.getPrincipal();

        List<OfferingResponseDTO> offerings = offeringService.getBySitter(sitter);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", offerings
        ));
    }

    @Operation(
            summary = "Obtener servicio por ID",
            description = "Devuelve los datos de un servicio individual registrado, si existe."
    )
    @GetMapping("/offering/{id}")
    public ResponseEntity<?> getByIdOffering(@PathVariable Long id) {
        try {
            OfferingResponseDTO dto = offeringService.getById(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", dto
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
    @Operation(
            summary = "Actualizar servicio",
            description = """
        Permite modificar los datos de un servicio individual registrado por el profesional.
        El campo 'description' debe coincidir exactamente con una de las variantes válidas según el tipo de servicio.
        Las variantes disponibles pueden consultarse en el endpoint /sitter/list/variant/descriptions.
        """
    )
    @PutMapping("/update/offering/{id}")
    public ResponseEntity<?> updateOffering(@PathVariable Long id,
                                    @Valid @RequestBody OfferingCreateDTO dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User sitter = (User) authentication.getPrincipal();

            OfferingResponseDTO updated = offeringService.update(id, sitter, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Servicio actualizado con éxito",
                    "data", updated
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
    @Operation(
            summary = "Eliminar servicio",
            description = "Permite desactivar un servicio individual registrado por el profesional."
    )
    @DeleteMapping("/delete/offering/{id}")
    public ResponseEntity<?> deleteOffering(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User sitter = (User) authentication.getPrincipal();

            offeringService.softDelete(id, sitter);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Servicio eliminado con éxito"
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
    @Operation(
            summary = "Activar servicio",
            description = "Permite activar un servicio individual registrado por el profesional."
    )
    @DeleteMapping("/active/offering/{id}")
    public ResponseEntity<?> activeOffering(@PathVariable Long id) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User sitter = (User) authentication.getPrincipal();

            offeringService.activeOffering(id, sitter);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Servicio activado con éxito"
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
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
            summary = "Obtener combo por ID",
            description = "Devuelve los datos completos de un combo registrado, incluyendo servicios asociados y advertencias."
    )
    @GetMapping("/combo/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            ComboOfferingResponseDTO dto = comboOfferingService.getById(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", dto
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @Operation(
            summary = "Actualizar combo",
            description = "Permite modificar los datos de un combo registrado por el profesional, incluyendo nombre, descripción y descuento."
    )
    @PutMapping("/update/combo/{id}")
    public ResponseEntity<?> updateCombo(@PathVariable Long id,
                                    @AuthenticationPrincipal User sitter,
                                    @RequestParam ComboOfferingUpdateDTO dto) {
        try {
            ComboOfferingResponseDTO updated = comboOfferingService.update(id, sitter, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Combo actualizado con éxito",
                    "data", updated
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
    @Operation(
            summary = "Eliminar combo",
            description = "Permite eliminar un combo registrado por el profesional."
    )
    @DeleteMapping("/delete/combo/{id}")
    public ResponseEntity<?> deleteCombo(@PathVariable Long id, @AuthenticationPrincipal User sitter) {
        try {
            comboOfferingService.delete(id, sitter);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Combo eliminado con éxito"
            ));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }
    @Operation(
            summary = "Listar combos propios",
            description = "Devuelve todos los combos registrados por el profesional autenticado."
    )
    @GetMapping("/my/combos")
    public ResponseEntity<?> getMyCombos(@AuthenticationPrincipal User sitter) {
        List<ComboOfferingResponseDTO> combos = comboOfferingService.getBySitter(sitter);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", combos
        ));
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
            description = "Devuelve las reglas de descuento creadas por el profesional autenticado, con sus condiciones y valores.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Listado exitoso de reglas de descuento"),
                    @ApiResponse(responseCode = "400", description = "Error de validación o rol no autorizado")
            }
    )
    @GetMapping("/list/discount/rule")
    public ResponseEntity<Map<String, Object>> getRulesForSitter() throws MyException {
        List<PlanDiscountRuleResponseDTO> rules = planDiscountRuleService.getRulesForSitter();

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", rules,
                "count", rules.size()
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
            summary = "Proponer reprogramación de un servicio",
            description = "Permite al profesional modificar el horario de un ítem activo. El dueño deberá aceptar o rechazar la propuesta"
    )
    @PutMapping("/booking/item/{itemId}/reschedule")
    public ResponseEntity<?> rescheduleItem(
            @PathVariable Long itemId,
            @RequestParam Long newScheduleId
    ) {
        User sitter = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            bookingService.rescheduleItem(itemId, newScheduleId, sitter);
            return ResponseEntity.ok(Map.of("status", "Propuesta de reprogramación de horario enviada"));
        } catch (MyException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
            summary = "Confirmar ítem de reserva",
            description = "Permite confirmar un ítem específico dentro de una reserva, validando el rol del profesional y actualizando el estado."
    )
    @PutMapping("/booking/item/{itemId}/confirm")
    public ResponseEntity<?> confirmItem(@PathVariable Long itemId, @AuthenticationPrincipal User sitter) {
        try {
            bookingService.confirmItem(itemId, sitter);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Ítem confirmado con éxito"
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
            summary = "Cancelar ítem de reserva",
            description = "Permite cancelar un ítem específico dentro de una reserva, registrando el motivo y actualizando el estado."
    )
    @PutMapping("/booking/item/{itemId}/cancel")
    public ResponseEntity<?> cancelItem(@PathVariable Long itemId,
                                        @AuthenticationPrincipal User sitter,
                                        @RequestParam String reason) {
        try {
            bookingService.cancelItem(itemId, sitter, reason);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Ítem cancelado con éxito"
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
}
