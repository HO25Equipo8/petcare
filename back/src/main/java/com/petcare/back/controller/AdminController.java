package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.BookingSimulationRequestDTO;
import com.petcare.back.domain.dto.request.ComboOfferingCreateDTO;
import com.petcare.back.domain.dto.request.PlanCreateDTO;
import com.petcare.back.domain.dto.request.OfferingCreateDTO;
import com.petcare.back.domain.dto.response.BookingSimulationResponseDTO;
import com.petcare.back.domain.dto.response.ComboOfferingResponseDTO;
import com.petcare.back.domain.dto.response.PlanResponseDTO;
import com.petcare.back.domain.dto.response.OfferingResponseDTO;
import com.petcare.back.domain.entity.PlanDiscountRule;
import com.petcare.back.domain.enumerated.OfferingEnum;
import com.petcare.back.domain.enumerated.OfferingVariantDescriptionEnum;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.PlanDiscountRuleRepository;
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
@RequestMapping("/admin")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class AdminController {


}
