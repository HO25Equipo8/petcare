package com.petcare.back.service;

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

    public void sendWelcomeEmail(String userEmail, String userName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(userEmail);
            message.setSubject("¡Petcare te dice Hola!");
            message.setText(buildWelcomeMessage(userName, userEmail));

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

    private String buildWelcomeMessage(String userName, String userEmail) {
        return String.format(
                "¡Hola %s!\n\n" +
                        "Tu cuenta ha sido creada exitosamente con el email: %s\n\n" +
                        "Ya puedes comenzar a usar todas nuestras funcionalidades.\n\n" +
                        "¡Gracias por unirte a nosotros!\n\n" +
                        "Saludos,\n" +
                        "El equipo de Petcare",
                userName != null ? userName : "Usuario",
                userEmail
        );
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
}
