package com.petcare.back.validation;

import com.petcare.back.domain.dto.request.FeedbackDTO;
import com.petcare.back.domain.dto.response.FeedbackResponseDTO;
import com.petcare.back.domain.entity.Booking;
import com.petcare.back.domain.entity.Feedback;
import com.petcare.back.domain.entity.User;
import com.petcare.back.domain.enumerated.BookingStatusEnum;
import com.petcare.back.domain.enumerated.FeedbackContext;
import com.petcare.back.domain.mapper.request.FeedbackMapper;
import com.petcare.back.exception.MyException;
import com.petcare.back.repository.BookingRepository;
import com.petcare.back.repository.FeedbackRepository;
import com.petcare.back.repository.UserRepository;
import com.petcare.back.service.FeedbackService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FeedbackMapper feedbackMapper;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    //Valida que un usuario no pueda dejarse feedback a sí mismo
    @Test
    void shouldThrowExceptionIfAuthorIsTarget() {
        User user = new User();
        user.setId(1L);

        FeedbackDTO dto = new FeedbackDTO(1L, 10L, 5, "Buen servicio", FeedbackContext.BOOKING);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null));

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setStatus(BookingStatusEnum.COMPLETADO);
        booking.setOwner(user); // necesario para evitar NPE

        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));

        MyException ex = assertThrows(MyException.class, () -> {
            feedbackService.giveFeedback(dto);
        });

        assertEquals("No puedes dejar feedback sobre ti mismo.", ex.getMessage());
    }

    //Test: feedback duplicado
    @Test
    void shouldThrowExceptionIfFeedbackAlreadyExists() {
        User author = new User(); author.setId(1L);
        User target = new User(); target.setId(2L);
        Booking booking = new Booking(); booking.setId(10L); booking.setStatus(BookingStatusEnum.COMPLETADO);
        booking.setOwner(author);
        booking.setProfessionals(List.of(target));

        FeedbackDTO dto = new FeedbackDTO(2L, 10L, 5, "Todo bien", FeedbackContext.BOOKING);

        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        lenient().when(feedbackRepository.existsByAuthorAndTargetAndBooking(author, target, booking)).thenReturn(true);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(author, null));

        MyException ex = assertThrows(MyException.class, () -> {
            feedbackService.giveFeedback(dto);
        });

        assertEquals("Ya dejaste feedback para esta reserva.", ex.getMessage());
    }

    //Test: reserva no completada
    @Test
    void shouldThrowExceptionIfBookingNotCompleted() {
        User author = new User(); author.setId(1L);
        User target = new User(); target.setId(2L);
        Booking booking = new Booking(); booking.setId(10L); booking.setStatus(BookingStatusEnum.PENDIENTE);
        booking.setOwner(author);

        FeedbackDTO dto = new FeedbackDTO(2L, 10L, 5, "Todo bien", FeedbackContext.BOOKING);

        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        lenient().when(feedbackRepository.existsByAuthorAndTargetAndBooking(author, target, booking)).thenReturn(false);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(author, null));

        MyException ex = assertThrows(MyException.class, () -> {
            feedbackService.giveFeedback(dto);
        });

        assertEquals("Solo podés dejar feedback si la reserva fue completada.", ex.getMessage());
    }

    //Test: rating fuera de rango
    @Test
    void shouldThrowExceptionIfRatingOutOfRange() {
        User author = new User(); author.setId(1L);
        User target = new User(); target.setId(2L);
        Booking booking = new Booking(); booking.setId(10L); booking.setStatus(BookingStatusEnum.COMPLETADO);
        booking.setOwner(author);

        FeedbackDTO dto = new FeedbackDTO(2L, 10L, 6, "Excelente", FeedbackContext.BOOKING);

        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        lenient().when(feedbackRepository.existsByAuthorAndTargetAndBooking(author, target, booking)).thenReturn(false);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(author, null));

        MyException ex = assertThrows(MyException.class, () -> {
            feedbackService.giveFeedback(dto);
        });

        assertEquals("La calificación debe estar entre 1 y 5 estrellas.", ex.getMessage());
    }

    //Test: feedback exitoso
    @Test
    void shouldSaveFeedbackSuccessfully() throws MyException {
        User author = new User(); author.setId(1L);
        User target = new User(); target.setId(2L);
        Booking booking = new Booking(); booking.setId(10L); booking.setStatus(BookingStatusEnum.COMPLETADO);
        booking.setOwner(author);
        booking.setProfessionals(List.of(target));

        FeedbackDTO dto = new FeedbackDTO(2L, 10L, 5, "Excelente trato", FeedbackContext.BOOKING);

        Feedback feedback = new Feedback();
        feedback.setId(100L);
        feedback.setAuthor(author);
        feedback.setTarget(target);
        feedback.setBooking(booking);
        feedback.setRating(5);
        feedback.setComment("Excelente trato");
        feedback.setContext(FeedbackContext.BOOKING);
        feedback.setCreatedAt(LocalDateTime.now());

        FeedbackResponseDTO responseDTO = new FeedbackResponseDTO(
                100L, "Franco", 5, "Excelente trato", FeedbackContext.BOOKING,
                "Reserva completada", "⭐ Opinión tras una reserva exitosa", LocalDateTime.now()
        );

        when(userRepository.findById(2L)).thenReturn(Optional.of(target));
        when(bookingRepository.findById(10L)).thenReturn(Optional.of(booking));
        when(feedbackRepository.existsByAuthorAndTargetAndBooking(author, target, booking)).thenReturn(false);
        when(feedbackRepository.save(any())).thenReturn(feedback);
        when(feedbackMapper.toDto(any())).thenReturn(responseDTO);

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(author, null));

        FeedbackResponseDTO result = feedbackService.giveFeedback(dto);

        assertEquals(100L, result.id());
        assertEquals("Excelente trato", result.comment());
    }
}
