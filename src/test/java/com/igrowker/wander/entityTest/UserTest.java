package com.igrowker.wander.entityTest;

import com.igrowker.wander.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;
    private Validator validator;

    @BeforeEach
    void setUp() {
        // Inicialización del objeto user antes de cada prueba
        user = new User();
        user.setId("1");
        user.setEmail("test@example.com");
        user.setName("TestUser");
        user.setPassword("securePass123");
        user.setRole("TOURIST");
        user.setAvatar("https://medvirturials.com/img/default-image.png");
        user.setEnabled(true);
        user.setPreferences(List.of("Hiking", "Photography"));
        user.setLocation("New York");
        user.setCreatedAt(new Date());
        user.setBookings(List.of(123L, 456L));
        user.setVerificationCode("ABC123");
        user.setVerificationCodeExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)); // 1 hora más
        user.setPasswordResetCode("XYZ789");
        user.setPasswordResetCodeExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)); // 1 hora más

        // Inicializa el validador
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testUserDetailsImplementation() {
        // Verificar autoridades
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_TOURIST", authorities.iterator().next().getAuthority());

        // Verificar que el username es el email
        assertEquals("test@example.com", user.getUsername());

        // Verificar que el estado de la cuenta es correcto
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testValidUser() {
        // Verificar que no hay errores de validación en un usuario válido
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "Se esperaban 0 errores de validación");
    }

    /*@Test
    void testInvalidEmail() {
        // Establecer un email inválido
        user.setEmail("invalid-email");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Se esperaban errores de validación por email inválido");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("must be a well-formed email address")));
    }

    @Test
    void testNullEmail() {
        // Establecer email como null
        user.setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Se esperaban errores de validación por email nulo");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The email is required")));
    }

    @Test
    void testShortPassword() {
        // Establecer una contraseña demasiado corta
        user.setPassword("123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Se esperaban errores de validación por contraseña demasiado corta");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The password is too short")));
    }

    @Test
    void testNullPassword() {
        // Establecer contraseña como null
        user.setPassword(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Se esperaban errores de validación por contraseña nula");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("The password is required")));
    }*/

    @Test
    void testDefaultValues() {
        User newUser = new User();
        // Verificar los valores predeterminados
        assertFalse(newUser.isEnabled(), "Se esperaba que el valor predeterminado de 'enabled' fuera false");
        assertEquals("TOURIST", newUser.getRole(), "Se esperaba que el rol predeterminado fuera 'TOURIST'");
        assertEquals("https://medvirturials.com/img/default-image.png", newUser.getAvatar(), "Se esperaba que el avatar predeterminado fuera la URL por defecto");
        assertNotNull(newUser.getCreatedAt(), "Se esperaba que la fecha de creación predeterminada estuviera configurada");
    }

    @Test
    void testVerificationCodeExpiration() {
        // Comprobar que el código de verificación tiene la fecha de expiración correctamente configurada
        Date expirationDate = user.getVerificationCodeExpiresAt();
        assertNotNull(expirationDate, "Se esperaba que la fecha de expiración del código de verificación no fuera nula");
        assertTrue(expirationDate.after(new Date()), "Se esperaba que la fecha de expiración fuera posterior a la fecha actual");
    }

    @Test
    void testPasswordResetCodeExpiration() {
        // Comprobar que el código de restablecimiento de contraseña tiene la fecha de expiración correctamente configurada
        Date expirationDate = user.getPasswordResetCodeExpiresAt();
        assertNotNull(expirationDate, "Se esperaba que la fecha de expiración del código de restablecimiento no fuera nula");
        assertTrue(expirationDate.after(new Date()), "Se esperaba que la fecha de expiración fuera posterior a la fecha actual");
    }
}