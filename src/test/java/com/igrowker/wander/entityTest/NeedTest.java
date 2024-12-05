package com.igrowker.wander.entityTest;

import com.igrowker.wander.entity.Need;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NeedTest {
    @Test
    void testGettersAndSetters() {
        // Arrange
        String id = "1";
        String experienceId = "exp-123";
        String type = "transportation";
        int quantity = 10;
        String status = "active";

        // Act
        Need need = new Need();
        need.setId(id);
        need.setExperienceId(experienceId);
        need.setType(type);
        need.setQuantity(quantity);
        need.setStatus(status);

        // Assert
        assertEquals(id, need.getId());
        assertEquals(experienceId, need.getExperienceId());
        assertEquals(type, need.getType());
        assertEquals(quantity, need.getQuantity());
        assertEquals(status, need.getStatus());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        String experienceId = "exp-123";
        Need need1 = new Need();
        need1.setId("1");
        need1.setExperienceId(experienceId);
        need1.setType("accommodation");
        need1.setQuantity(5);
        need1.setStatus("pending");

        Need need2 = new Need();
        need2.setId("1");
        need2.setExperienceId(experienceId);
        need2.setType("accommodation");
        need2.setQuantity(5);
        need2.setStatus("pending");

        // Act & Assert
        assertEquals(need1, need2); // Verifica que equals funciona correctamente
        assertEquals(need1.hashCode(), need2.hashCode()); // Verifica que hashCode coincide
    }

    @Test
    void testToString() {
        // Arrange
        String experienceId = "exp-123";
        Need need = new Need();
        need.setId("1");
        need.setExperienceId(experienceId);
        need.setType("food");
        need.setQuantity(3);
        need.setStatus("completed");

        // Act
        String toStringResult = need.toString();

        // Assert
        assertTrue(toStringResult.contains("1"));
        assertTrue(toStringResult.contains("exp-123"));
        assertTrue(toStringResult.contains("food"));
        assertTrue(toStringResult.contains("3"));
        assertTrue(toStringResult.contains("completed"));
    }
}