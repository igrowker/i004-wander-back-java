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
        assertNotNull(experience.getCreatedAt());
        assertTrue(experience.isStatus());
        assertNull(experience.getExperienceImages());
    }

    @Test
    void testAllArgsConstructor() {
        // Arrange
        String id = "1";
        String title = "Experience Title";
        String description = "Description of the experience";
        List<String> location = Arrays.asList("United States", "New York", "40°38'43.4\"N", "73°59'57.6\"W");
        String hostId = "host123";
        Double price = 199.99;
        List<String> availabilityDates = Arrays.asList("2023-12-01", "2023-12-02");
        List<String> tags = Arrays.asList("Adventure", "Nature");
        Double rating = 4.5;
        int capacity = 20;
        Date createdAt = new Date();
        boolean status = false;
        List<String> expImag = List.of("imagen1", "imagen2", "imagen3");

        // Act
        ExperienceEntity experience = new ExperienceEntity(id, title, description, location, hostId, price, availabilityDates, tags, rating, capacity, createdAt, status, expImag);

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
        assertEquals(expImag, experience.getExperienceImages());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        ExperienceEntity experience = new ExperienceEntity();
        String id = "1";
        String title = "New Experience";
        String description = "Exciting description";
        List<String> location = Arrays.asList("England", "London", "51°30'58.1\"N", "0°08'28.0\"W");
        String hostId = "host456";
        Double price = 299.99;
        List<String> availabilityDates = Arrays.asList("2023-12-10", "2023-12-11");
        List<String> tags = Arrays.asList("Relaxation", "Luxury");
        Double rating = 4.9;
        int capacity = 10;
        Date createdAt = new Date();
        boolean status = true;
        List<String> expImag = List.of("imagen1", "imagen2", "imagen3");

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
        experience.setExperienceImages(expImag);

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
        assertEquals(expImag, experience.getExperienceImages());
    }

    @Test
    void testEqualityAndHashCode() {
        // Arrange
        ExperienceEntity experience1 = new ExperienceEntity("1", "Title", "Description", Arrays.asList("England", "London", "51°30'58.1\"N", "0°08'28.0\"W"), "host123", 100.0, null, null, 4.5, 10, new Date(), true, List.of("imagen1"));
        ExperienceEntity experience2 = new ExperienceEntity("1", "Title", "Description", Arrays.asList("England", "London", "51°30'58.1\"N", "0°08'28.0\"W"), "host123", 100.0, null, null, 4.5, 10, new Date(), true, List.of("imagen1"));

        // Act & Assert
        assertEquals(experience1, experience2);
        assertEquals(experience1.hashCode(), experience2.hashCode());
    }
}
