package com.petcare.back.controller;

import com.petcare.back.service.ScheduleConfigService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class AdminController {

    private final ScheduleConfigService scheduleConfigService;

    //Metodo para cambiar el estado a expirado de las reservas anteriores al dia de hoy
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/schedules/expire-now")
    public ResponseEntity<String> expireNow() {
        int count = scheduleConfigService.expireOldSchedules();
        int count1 = scheduleConfigService.deleteUnlinkedExpiredSchedules();
        return ResponseEntity.ok("Se expiraron " + count + " horarios y se eliminaron " + count1 + " sin reservas.");
    }
}
