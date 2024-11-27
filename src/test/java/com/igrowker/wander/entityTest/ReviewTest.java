package com.igrowker.wander.entityTest;

import com.igrowker.wander.entity.ReviewEntity;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewTest {

    @Test
    void testDefaultConstructor() {
        // Crear una instancia usando el constructor por defecto
        ReviewEntity review = new ReviewEntity();

        // Verificar que los valores predeterminados están correctamente asignados
        assertNull(review.getId());
        assertNull(review.getExperienceId());
        assertNull(review.getUserId());
        assertNull(review.getRating());
        assertNull(review.getComment());
        assertNotNull(review.getCreatedAt());
    }

    @Test
    void testAllArgsConstructor() {
        // Crear una instancia usando el constructor con todos los argumentos
        Date createdAt = new Date();
        ReviewEntity review = new ReviewEntity("123", "exp-001", "user-001", 4.5, "Great experience!", createdAt);

        // Verificar que los valores se han asignado correctamente
        assertEquals("123", review.getId());
        assertEquals("exp-001", review.getExperienceId());
        assertEquals("user-001", review.getUserId());
        assertEquals(4.5, review.getRating());
        assertEquals("Great experience!", review.getComment());
        assertEquals(createdAt, review.getCreatedAt());
    }

    @Test
    void testSettersAndGetters() {
        // Crear una instancia con el constructor por defecto
        ReviewEntity review = new ReviewEntity();

        // Asignar valores usando los setters
        review.setId("456");
        review.setExperienceId("exp-002");
        review.setUserId("user-002");
        review.setRating(5.0);
        review.setComment("Amazing experience!");

        // Verificar que los valores se han asignado correctamente
        assertEquals("456", review.getId());
        assertEquals("exp-002", review.getExperienceId());
        assertEquals("user-002", review.getUserId());
        assertEquals(5.0, review.getRating());
        assertEquals("Amazing experience!", review.getComment());
    }

    @Test
    void testNoArgsConstructor() {
        // Crear una instancia usando el constructor sin argumentos
        ReviewEntity review = new ReviewEntity();

        // Verificar que los atributos están correctamente inicializados
        assertNull(review.getId());
        assertNull(review.getExperienceId());
        assertNull(review.getUserId());
        assertNull(review.getRating());
        assertNull(review.getComment());
        assertNotNull(review.getCreatedAt());
    }
}
