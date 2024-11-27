package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.entity.User;
import com.igrowker.wander.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendVerificationEmail(User user) {
        String subject = "Verificación de cuenta";
        String htmlMessage = generateVerificationEmailContent(user.getVerificationCode());
        sendEmail(user.getEmail(), subject, htmlMessage);
    }

    @Override
    public void sendPasswordResetEmail(User user) {
        String subject = "Restablecimiento de contraseña";
        String htmlMessage = generatePasswordResetEmailContent(user.getPasswordResetCode());
        sendEmail(user.getEmail(), subject, htmlMessage);
    }

    private void sendEmail(String to, String subject, String htmlMessage) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlMessage, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalArgumentException("Error enviando email: " + e.getMessage());
        }
    }

    private String generateVerificationEmailContent(String verificationCode) {
        return "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #FF9D14; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">¡Bienvenido a Wander!</h2>"
                + "<p style=\"font-size: 16px;\">Por favor ingresa el siguiente código debajo para continuar:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Código de Verificación:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    private String generatePasswordResetEmailContent(String resetCode) {
        return "<html><body>"
                + "<h3>Solicitud de restablecimiento de contraseña</h3>"
                + "<p>Haz clic en el enlace a continuación para restablecer tu contraseña:</p>"
                + "<h3 style=\"color: #333;\">Código de Verificación:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + resetCode + "</p>"
                + "</body></html>";
    }

}
