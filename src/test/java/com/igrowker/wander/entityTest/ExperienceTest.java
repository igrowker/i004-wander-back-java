package com.igrowker.wander.entityTest;

import com.igrowker.wander.entity.ExperienceEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExperienceTest {
    @Test
    void testNoArgsConstructor() {
        // Arrange & Act
        ExperienceEntity experience = new ExperienceEntity();

        // Assert
        assertNotNull(experience);
        assertNull(experience.getId());
        assertNull(experience.getTitle());
        assertNull(experience.getDescription());
        assertNull(experience.getLocation());
        assertNull(experience.getHostId());
        assertNull(experience.getPrice());
        assertNull(experience.getAvailabilityDates());
        assertNull(experience.getTags());
        assertNull(experience.getRating());
        assertEquals(0, experience.getCapacity());
        assertNotNull(experience.getCreatedAt()); // Default value is the current date
        assertTrue(experience.isStatus());       // Default value is true
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        String id = "1";
        String title = "Experience Title";
        String description = "Description of the experience";
        String location = "New York";
        String hostId = "host123";
        Double price = 199.99;
        List<String> availabilityDates = Arrays.asList("2023-12-01", "2023-12-02");
        List<String> tags = Arrays.asList("Adventure", "Nature");
        Double rating = 4.5;
        int capacity = 20;
        Date createdAt = new Date();
        boolean status = false;

        // Act
        ExperienceEntity experience = new ExperienceEntity(id, title, description, location, hostId, price, availabilityDates, tags, rating, capacity, createdAt, status);

        // Assert
        assertEquals(id, experience.getId());
        assertEquals(title, experience.getTitle());
        assertEquals(description, experience.getDescription());
        assertEquals(location, experience.getLocation());
        assertEquals(hostId, experience.getHostId());
        assertEquals(price, experience.getPrice());
        assertEquals(availabilityDates, experience.getAvailabilityDates());
        assertEquals(tags, experience.getTags());
        assertEquals(rating, experience.getRating());
        assertEquals(capacity, experience.getCapacity());
        assertEquals(createdAt, experience.getCreatedAt());
        assertFalse(experience.isStatus());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        ExperienceEntity experience = new ExperienceEntity();
        String id = "1";
        String title = "New Experience";
        String description = "Exciting description";
        String location = "London";
        String hostId = "host456";
        Double price = 299.99;
        List<String> availabilityDates = Arrays.asList("2023-12-10", "2023-12-11");
        List<String> tags = Arrays.asList("Relaxation", "Luxury");
        Double rating = 4.9;
        int capacity = 10;
        Date createdAt = new Date();
        boolean status = true;

        // Act
        experience.setId(id);
        experience.setTitle(title);
        experience.setDescription(description);
        experience.setLocation(location);
        experience.setHostId(hostId);
        experience.setPrice(price);
        experience.setAvailabilityDates(availabilityDates);
        experience.setTags(tags);
        experience.setRating(rating);
        experience.setCapacity(capacity);
        experience.setCreatedAt(createdAt);
        experience.setStatus(status);

        // Assert
        assertEquals(id, experience.getId());
        assertEquals(title, experience.getTitle());
        assertEquals(description, experience.getDescription());
        assertEquals(location, experience.getLocation());
        assertEquals(hostId, experience.getHostId());
        assertEquals(price, experience.getPrice());
        assertEquals(availabilityDates, experience.getAvailabilityDates());
        assertEquals(tags, experience.getTags());
        assertEquals(rating, experience.getRating());
        assertEquals(capacity, experience.getCapacity());
        assertEquals(createdAt, experience.getCreatedAt());
        assertTrue(experience.isStatus());
    }

    @Test
    void testEqualityAndHashCode() {
        // Arrange
        ExperienceEntity experience1 = new ExperienceEntity("1", "Title", "Description", "Location", "host123", 100.0, null, null, 4.5, 10, new Date(), true);
        ExperienceEntity experience2 = new ExperienceEntity("1", "Title", "Description", "Location", "host123", 100.0, null, null, 4.5, 10, new Date(), true);

        // Act & Assert
        assertEquals(experience1, experience2);
        assertEquals(experience1.hashCode(), experience2.hashCode());
    }
}
