package com.igrowker.wander.serviceImplTest;

import com.igrowker.wander.dto.experience.RequestExperienceDto;
import com.igrowker.wander.dto.experience.ResponseExperienceDto;
import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.InvalidDataException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.ExperienceRepository;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.serviceimpl.ExperienceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ExperienceServiceImplTest {

    @InjectMocks
    private ExperienceServiceImpl experienceService;

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExperience_Success() {
        RequestExperienceDto request = new RequestExperienceDto();
        request.setTitle("Excursión");
        request.setDescription("Descripción de prueba");
        request.setLocation(Collections.singletonList("España"));
        request.setPrice(100.0);
        request.setAvailabilityDates(List.of(String.valueOf(LocalDateTime.now())));
        request.setTags(List.of("aventura"));
        request.setCapacity(10);

        User user = new User();
        user.setId("user123");
        user.setRole("PROVIDER");

        ExperienceEntity experience = new ExperienceEntity();
        experience.setId("exp123");

        when(experienceRepository.save(any(ExperienceEntity.class))).thenReturn(experience);

        ExperienceEntity result = experienceService.createExperience(request, user);

        assertNotNull(result);
        assertEquals("exp123", result.getId());
        verify(experienceRepository, times(1)).save(any(ExperienceEntity.class));
    }

    @Test
    void createExperience_InvalidUserRole() {
        RequestExperienceDto request = new RequestExperienceDto();
        User user = new User();
        user.setId("user123");
        user.setRole("USER"); // No es PROVIDER

        Exception exception = assertThrows(InvalidDataException.class, () -> experienceService.createExperience(request, user));

        assertEquals("El usuario no tiene permisos para crear una experiencia.", exception.getMessage());
        verifyNoInteractions(experienceRepository);
    }

//    @Test
//    void getExperiences_ByCityAndTag() {
//        List<String> tags = List.of("aventura");
//        List<String> location = List.of("España", "Madrid");
//
//        ExperienceEntity experience = new ExperienceEntity();
//        experience.setId("exp123");
//        experience.setTitle("Excursión");
//
//        when(experienceRepository.findByCityAndTagsIn("Madrid", tags)).thenReturn(List.of(experience));
//
//        List<ExperienceEntity> result = experienceService.getExperiences(location, null, null, tags);
//
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertEquals("exp123", result.get(0).getId());
//        verify(experienceRepository, times(1)).findByCityAndTagsIn("Madrid", tags);
//    }

    @Test
    void getExperienceById_Success() {
        ExperienceEntity experience = new ExperienceEntity();
        experience.setId("exp123");

        when(experienceRepository.findById("exp123")).thenReturn(Optional.of(experience));

        ExperienceEntity result = experienceService.getExperienceById("exp123");

        assertNotNull(result);
        assertEquals("exp123", result.getId());
        verify(experienceRepository, times(1)).findById("exp123");
    }

    @Test
    void getExperienceById_NotFound() {
        when(experienceRepository.findById("exp123")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> experienceService.getExperienceById("exp123"));

        assertEquals("No se encontró la experiencia con el ID: exp123", exception.getMessage());
        verify(experienceRepository, times(1)).findById("exp123");
    }

    /*@Test
    void updateExperience_Success() {
        ExperienceEntity existingExperience = new ExperienceEntity();
        existingExperience.setId("exp123");
        existingExperience.setHostId("host123");

        ExperienceEntity updatedExperienceData = new ExperienceEntity();
        updatedExperienceData.setTitle("Nueva excursión");

        User user = new User();
        user.setId("host123");

        when(experienceRepository.findById("exp123")).thenReturn(Optional.of(existingExperience));
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);
        when(experienceRepository.save(any(ExperienceEntity.class))).thenReturn(existingExperience);

        ExperienceEntity result = experienceService.updateExperience("exp123", updatedExperienceData);

        assertNotNull(result);
        assertEquals("Nueva excursión", result.getTitle());
        verify(experienceRepository, times(1)).save(existingExperience);
    }

    @Test
    void updateExperience_UnauthorizedUser() {
        ExperienceEntity existingExperience = new ExperienceEntity();
        existingExperience.setId("exp123");
        existingExperience.setHostId("host123");

        ExperienceEntity updatedExperienceData = new ExperienceEntity();

        User user = new User();
        user.setId("host456"); // ID no coincide

        when(experienceRepository.findById("exp123")).thenReturn(Optional.of(existingExperience));
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(user);

        Exception exception = assertThrows(SecurityException.class, () -> {
            experienceService.updateExperience("exp123", updatedExperienceData);
        });

        assertEquals("El usuario no tiene permiso para editar esta experiencia.", exception.getMessage());
        verify(experienceRepository, times(0)).save(any(ExperienceEntity.class));
    }*/

    @Test
    void getExperiencesByHost_Success() {
        ExperienceEntity experience = new ExperienceEntity();
        experience.setId("exp123");

        when(userRepository.findById("host123")).thenReturn(Optional.of(new User()));
        when(experienceRepository.findByHostId("host123")).thenReturn(List.of(experience));

        List<ResponseExperienceDto> result = experienceService.getExperiencesByHost("host123");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("exp123", result.get(0).getId());
    }

    @Test
    void getExperiencesByHost_NotFound() {
        when(userRepository.findById("host123")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> experienceService.getExperiencesByHost("host123"));

        assertEquals("No se encontró el anfitrión con ID: host123", exception.getMessage());
    }
}