package com.petcare.back.service;

import com.petcare.back.domain.dto.request.FeedbackDTO;
import com.petcare.back.domain.dto.response.FeedbackResponseDTO;
import com.petcare.back.domain.entity.Booking;
import com.petcare.back.domain.entity.Feedback;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.BookingStatusEnum;
import com.petcare.back.domain.mapper.request.FeedbackMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.BookingRepository;
import com.petcare.back.repository.FeedbackRepository;
import com.petcare.back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final FeedbackMapper feedbackMapper;
    private final BookingRepository bookingRepository;

    public List<FeedbackResponseDTO> getFeedbackForUser(Long id) throws MyException {

        User target = userRepository.findById(id)
                .orElseThrow(() -> new MyException("Usuario no encontrado"));

        List<Feedback> feedbacks = feedbackRepository.findByTargetOrderByCreatedAtDesc(target);
        return feedbackMapper.toDtoList(feedbacks);
    }
    public FeedbackResponseDTO giveFeedback(FeedbackDTO dto) throws MyException {
        User author = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User target = userRepository.findById(dto.targetUserId())
                .orElseThrow(() -> new MyException("Usuario destino no encontrado"));

        Booking booking = bookingRepository.findById(dto.bookingId())
                .orElseThrow(() -> new MyException("Reserva no encontrada"));

        validateFeedbackRequest(author, target, dto, booking);

        Feedback feedback = new Feedback();
        feedback.setAuthor(author);
        feedback.setTarget(target);
        feedback.setBooking(booking);
        feedback.setRating(dto.rating());
        feedback.setComment(dto.comment());
        feedback.setContext(dto.context());
        feedback.setCreatedAt(LocalDateTime.now());

        Feedback saved = feedbackRepository.save(feedback);
        return feedbackMapper.toDto(saved);
    }

    private void validateFeedbackRequest(User author, User target, FeedbackDTO dto, Booking booking) throws MyException {
        if (author.getId().equals(target.getId())) {
            throw new MyException("No puedes dejar feedback sobre ti mismo.");
        }

        if (dto.rating() < 1 || dto.rating() > 5) {
            throw new MyException("La calificación debe estar entre 1 y 5 estrellas.");
        }

        if (dto.rating() <= 2 && (dto.comment() == null || dto.comment().isBlank())) {
            throw new MyException("Por favor, agrega un comentario si la calificación es baja.");
        }

        if (!booking.getStatus().equals(BookingStatusEnum.COMPLETADO)) {
            throw new MyException("Solo podés dejar feedback si la reserva fue completada.");
        }

        boolean authorParticipa = booking.getOwner().equals(author) || booking.getProfessionals().contains(author);
        boolean targetParticipa = booking.getOwner().equals(target) || booking.getProfessionals().contains(target);

        if (!authorParticipa || !targetParticipa) {
            throw new MyException("Ambos usuarios deben haber participado en la misma reserva.");
        }

        if (feedbackRepository.existsByAuthorAndTargetAndBooking(author, target, booking)) {
            throw new MyException("Ya dejaste feedback para esta reserva.");
        }
    }
}