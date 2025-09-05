package com.petcare.back.controller;

import com.petcare.back.domain.dto.request.FeedbackDTO;
import com.petcare.back.domain.dto.request.UserPublicProfileDTO;
import com.petcare.back.domain.dto.response.FeedbackResponseDTO;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.Role;
import com.petcare.back.domain.mapper.request.UserFeedbackMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.service.FeedbackService;
import com.petcare.back.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class UserInteractionController {

    private final FeedbackService feedbackService;
    private final UserService userService;
    private final UserFeedbackMapper userFeedbackMapper;

    //Dar Feedback
    @PostMapping("/feedback")
    public ResponseEntity<?> giveFeedback(@RequestBody FeedbackDTO dto) throws MyException {
        FeedbackResponseDTO feedback = feedbackService.giveFeedback(dto);
        return ResponseEntity.ok(Map.of("status", "success", "data", feedback));
    }

    //Ver el perfil público
    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getPublicProfile(@PathVariable Long id) throws MyException {
        User user = userService.getPublicProfile(id);
        UserPublicProfileDTO dto = userFeedbackMapper.toPublicProfile(user);
        return ResponseEntity.ok(dto);
    }

    //Ver su listado de feedback recibidos
    @GetMapping("/{id}/feedback")
    public ResponseEntity<?> getFeedback(@PathVariable Long id) throws MyException {
        List<FeedbackResponseDTO> feedbacks = feedbackService.getFeedbackForUser(id); // ✅
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/profiles")
    public ResponseEntity<?> getPublicProfiles(@RequestParam Role role) throws MyException {
        User requester = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // ❌ No puede ver perfiles de su mismo rol
        if (requester.getRole() == role) {
            throw new MyException("No puedes ver perfiles de otros usuarios con tu mismo rol.");
        }

        List<User> users = userService.findByRole(role);
        List<UserPublicProfileDTO> dtos = users.stream()
                .map(userFeedbackMapper::toPublicProfile)
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/sitters/top")
    public ResponseEntity<?> getTopSitters(@RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(0, size);
        List<User> topSitters = userService.findTopSitters(pageable);
        List<UserPublicProfileDTO> dtos = topSitters.stream()
                .map(userFeedbackMapper::toPublicProfile)
                .toList();
        return ResponseEntity.ok(dtos);
    }
}
