package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.BookingSimulationRequestDTO;
import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.dto.request.ScheduleConfigCreateDTO;
import com.petcare.back.domain.dto.response.*;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.OfferingVariantDescriptionEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/register/discount/rule")
    public ResponseEntity<?> createRule(@RequestBody PlanDiscountRule rule) throws MyException {
        PlanDiscountRule created = planDiscountRuleService.createRule(rule);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Regla de descuento creada con éxito",
                "data", created
        ));
    }

    @GetMapping("/list/discount/rule")
    public ResponseEntity<?> getAllRules() {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", planDiscountRuleService.getAllRules()
        ));
    }

    @DeleteMapping("/delete/discount/rule/{id}")
    public ResponseEntity<?> deleteRule(@PathVariable Long id) {
        planDiscountRuleService.deleteRule(id);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Regla de descuento eliminada con éxito"
        ));
    }
    @GetMapping("/list/variant/descriptions")
    public Map<OfferingEnum, List<String>> getDescriptionsByOffering() {
        return Arrays.stream(OfferingVariantDescriptionEnum.values())
                .collect(Collectors.groupingBy(
                        OfferingVariantDescriptionEnum::getBaseOffering,
                        Collectors.mapping(OfferingVariantDescriptionEnum::getDescription, Collectors.toList())
                ));
    }
    @PostMapping("/simulation")
    public ResponseEntity<?> simulate(@RequestBody BookingSimulationRequestDTO dto) {
        BookingSimulationResponseDTO bookingSimulationResponseDTO = bookingService.simulateBooking(dto);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Simulacion creada con éxito",
                "data", bookingSimulationResponseDTO
        ));
    }

    //Metodo para cambiar el estado a expirado de las reservas anteriores al dia de hoy
    @PostMapping("/schedules/expire-now")
    public ResponseEntity<String> expireNow() {
        int count = scheduleConfigService.expireOldSchedules();
        return ResponseEntity.ok("Se expiraron " + count + " horarios disponibles anteriores a hoy");
    }
}
