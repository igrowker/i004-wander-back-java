package com.igrowker.wander.serviceImplTest;

import com.igrowker.wander.entity.User;
import com.igrowker.wander.serviceimpl.EmailServiceImpl;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmailServiceImplTest {
    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendVerificationEmail() {
        // Preparar datos de prueba
        User user = new User();
        user.setEmail("test@example.com");
        user.setVerificationCode("123456");

        MimeMessage mockMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mockMessage);

        // Ejecutar el método
        emailService.sendVerificationEmail(user);

        // Verificar que se envió el correo
        verify(emailSender, times(1)).send(mockMessage);
    }

    @Test
    void testSendPasswordResetEmail() {
        // Preparar datos de prueba
        User user = new User();
        user.setEmail("reset@example.com");
        user.setPasswordResetCode("654321");

        MimeMessage mockMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mockMessage);

        // Ejecutar el método
        emailService.sendPasswordResetEmail(user);

        // Verificar que se envió el correo
        verify(emailSender, times(1)).send(mockMessage);
    }

    /*@Test
    void testSendEmailThrowsException() {
        // Preparar datos de prueba
        MimeMessage mockMessage = mock(MimeMessage.class);
        when(emailSender.createMimeMessage()).thenReturn(mockMessage);

        doThrow(new RuntimeException("Simulated failure"))
                .when(emailSender).send(mockMessage);

        // Verificar que lanza la excepción adecuada
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendVerificationEmail(new User());
        });

        assertTrue(exception.getMessage().contains("Error enviando email"));
    }*/

    @Test
    void testGenerateVerificationEmailContent() {
        String verificationCode = "123456";
        String result = emailService.generateVerificationEmailContent(verificationCode);

        assertNotNull(result);
        assertTrue(result.contains("123456"));
        assertTrue(result.contains("¡Bienvenido a Wander!"));
    }

    @Test
    void testGeneratePasswordResetEmailContent() {
        String resetCode = "654321";
        String result = emailService.generatePasswordResetEmailContent(resetCode);

        assertNotNull(result);
        assertTrue(result.contains("654321"));
        assertTrue(result.contains("Solicitud de restablecimiento de contraseña"));
    }
}
