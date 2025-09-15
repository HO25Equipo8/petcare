package com.petcare.back.service;

import com.petcare.back.domain.dto.request.BookingDataByEmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendWelcomeEmail(String userEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(userEmail);
            message.setSubject("¡Petcare te dice Hola!");
            message.setText(buildWelcomeMessage(userEmail));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando email de bienvenida: " + e.getMessage());
        }
    }

    public void sendWelcomeEmail(String userEmail, String userName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(userEmail);
            message.setSubject("¡Petcare te dice Hola, " + userName+ "!");
            message.setText(buildWelcomeMessage(userEmail));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando email de bienvenida: " + e.getMessage());
        }
    }

    public void sendPassRecoverEmail(String userEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(userEmail);
            message.setSubject("¡Te olvidaste la contraseña!");
            message.setText(buildPassRecoverMessage(userEmail));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando email para recuperar constraseña: " + e.getMessage());
        }
    }

    public void sendAdminNotification(String userEmail, String userName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(adminEmail);
            message.setSubject("Nuevo usuario registrado");
            message.setText(buildAdminNotificationMessage(userName, userEmail));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando notificación al admin: " + e.getMessage());
        }
    }

    public void sendBookingConfirmationEmail(BookingDataByEmailDTO emailDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emailDTO.userEmail());
            message.setSubject("Reserva Confirmada - PetCare");
            message.setText(buildConfirmationMessage(
                    emailDTO.ownerName(),
                    emailDTO.professionalName(),
                    emailDTO.petName(),
                    emailDTO.sessionDate(),
                    emailDTO.startTime(),
                    emailDTO.endTime()));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando email de confirmación: " + e.getMessage());
        }
    }

    public void sendBookingCancellationEmail(BookingDataByEmailDTO emailDTO, String reason) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emailDTO.userEmail());
            message.setSubject("Reserva Cancelada - PetCare");
            message.setText(buildCancellationMessage(
                    emailDTO.ownerName(),
                    emailDTO.professionalName(),
                    emailDTO.petName(),
                    emailDTO.sessionDate(),
                    emailDTO.startTime(),
                    emailDTO.endTime(),
                    reason));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando email de cancelación: " + e.getMessage());
        }
    }

    public void sendBookingRescheduleEmail(BookingDataByEmailDTO emailDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(emailDTO.userEmail());
            message.setSubject("Propuesta de Reprogramación - PetCare");
            message.setText(buildRescheduleMessage(
                    emailDTO.ownerName(),
                    emailDTO.professionalName(),
                    emailDTO.petName(),
                    emailDTO.sessionDate(),
                    emailDTO.startTime(),
                    emailDTO.endTime()));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando email de reprogramación: " + e.getMessage());
        }
    }

    private String buildWelcomeMessage(String userName) {
        return """
            🐾 ¡Bienvenido a PetCare! 🐶🐱🐰
            
            Gracias por registrarte en nuestra plataforma, %s. 
            Ya podés acceder a todas las funcionalidades para cuidar, conectar y disfrutar junto a tus mascotas.
            
            ✨ Reservá servicios con profesionales verificados  
            🦴 Compartí experiencias y opiniones  
            📅 Organizá tus horarios y seguimientos  
            💬 Recibí feedback y construí tu reputación  
            
            ¡Nos alegra tenerte con nosotros! 💚
            """.formatted(userName != null ? userName : "Usuario");
    }

    private String buildPassRecoverMessage(String userEmail) {
        return String.format(
                "¡Hola!\n\n" +
                        "Solicitaste recuperar la contraseña para usuario con email: %s\n\n" +
                        "En breve podrás setear una nueva contraseña.\n\n" +
                        "Saludos,\n" +
                        "El equipo de Petcare",
                userEmail
        );
    }

    private String buildAdminNotificationMessage(String userName, String userEmail) {
        return String.format(
                "Nuevo usuario registrado:\n\n" +
                        "Email: %s\n" +
                        "Nombre: %s\n" +
                        "Fecha de registro: %s\n\n" +
                        "Saludos,\n" +
                        "Sistema de notificaciones",
                userEmail,
                userName != null ? userName : "No especificado",
                java.time.LocalDateTime.now().toString()
        );
    }

    private String buildConfirmationMessage(String ownerName, String professionalName, String petName,
                                            String sessionDate, String startTime, String endTime) {
        return """
            Hola %s,
            
            Tu reserva con %s ha sido confirmada 🎉
            🐾 Mascota: %s
            📅 Fecha: %s
            🕒 Horario: %s a %s
            
            Gracias por confiar en nosotros. Si necesitás modificar algo, podés hacerlo desde tu perfil.
            
            ¡Nos vemos pronto!
            El equipo de PetCare
            """.formatted(
                ownerName != null ? ownerName : "Usuario",
                professionalName != null ? professionalName : "el profesional",
                petName != null ? petName : "tu mascota",
                sessionDate != null ? sessionDate : "por confirmar",
                startTime != null ? startTime : "por confirmar",
                endTime != null ? endTime : "por confirmar"
        );
    }

    private String buildCancellationMessage(String ownerName, String professionalName, String petName,
                                            String sessionDate, String startTime, String endTime, String reason) {
        String reasonText = (reason != null && !reason.trim().isEmpty()) ?
                "\nMotivo: " + reason : "";

        return """
            Hola %s,
            
            Lamentamos informarte que la reserva con %s ha sido cancelada.
            🐾 Mascota: %s
            📅 Fecha original: %s
            🕒 Horario: %s a %s
            
            Podés volver a reservar desde tu perfil o explorar otros profesionales disponibles.
            
            Gracias por tu comprensión,
            El equipo de PetCare
            """.formatted(
                ownerName != null ? ownerName : "Usuario",
                professionalName != null ? professionalName : "el profesional",
                petName != null ? petName : "tu mascota",
                sessionDate != null ? sessionDate : "por confirmar",
                startTime != null ? startTime : "por confirmar",
                endTime != null ? endTime : "por confirmar",
                reasonText
        );
    }

    private String buildRescheduleMessage(String ownerName, String professionalName, String petName,
                                          String newDate, String newStartTime, String newEndTime) {
        return """
            Hola %s,
            
            %s ha propuesto una reprogramación para tu reserva.
            🐾 Mascota: %s
            📅 Nueva fecha: %s
            🕒 Nuevo horario: %s a %s
            
            Podés aceptar o rechazar esta propuesta desde tu perfil. La reserva original quedará en pausa hasta que respondas.
            
            Gracias por tu colaboración,
            El equipo de PetCare
            """.formatted(
                ownerName != null ? ownerName : "Usuario",
                professionalName != null ? professionalName : "El profesional",
                petName != null ? petName : "tu mascota",
                newDate != null ? newDate : "por confirmar",
                newStartTime != null ? newStartTime : "por confirmar",
                newEndTime != null ? newEndTime : "por confirmar"
        );
    }
}
