package com.igrowker.wander.serviceImplTest;

import com.igrowker.wander.entity.Need;
import com.igrowker.wander.repository.NeedRepository;
import com.igrowker.wander.service.NeedService;
import com.igrowker.wander.serviceimpl.NeedServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NeedServiceImplTest {
    @Mock
    private NeedRepository needRepository;

    @InjectMocks
    private NeedServiceImpl needService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNeedsByExperienceId() {
        // Datos de prueba
        String experienceId = "exp123";
        Need need1 = new Need();
        need1.setId("1");
        need1.setExperienceId(experienceId);
        Need need2 = new Need();
        need2.setId("2");
        need2.setExperienceId(experienceId);

        List<Need> mockNeeds = Arrays.asList(need1, need2);

        // Comportamiento simulado del repositorio
        when(needRepository.findByExperienceId(experienceId)).thenReturn(mockNeeds);

        // Llamada al servicio
        List<Need> needs = needService.getNeedsByExperienceId(experienceId);

        // Verificaciones
        assertNotNull(needs);
        assertEquals(2, needs.size());
        assertEquals(experienceId, needs.get(0).getExperienceId());
        assertEquals(experienceId, needs.get(1).getExperienceId());

        // Verificar que el repositorio fue llamado una vez
        verify(needRepository, times(1)).findByExperienceId(experienceId);
    }

    @Test
    void testGetNeedsByExperienceIdNoResults() {
        // Datos de prueba
        String experienceId = "exp123";

        // Comportamiento simulado del repositorio (sin resultados)
        when(needRepository.findByExperienceId(experienceId)).thenReturn(Arrays.asList());

        // Llamada al servicio
        List<Need> needs = needService.getNeedsByExperienceId(experienceId);

        // Verificaciones
        assertNotNull(needs);
        assertTrue(needs.isEmpty());

        // Verificar que el repositorio fue llamado una vez
        verify(needRepository, times(1)).findByExperienceId(experienceId);
    }

    @Test
    void testGetNeedsByExperienceIdWithNullExperienceId() {
        // Llamada al servicio con un ID de experiencia nulo
        List<Need> needs = needService.getNeedsByExperienceId(null);

        // Verificación: Se debe devolver una lista vacía o lanzar una excepción si lo especificas en el código
        assertNotNull(needs);
        assertTrue(needs.isEmpty());

        // Verificar que el repositorio fue llamado una vez
        verify(needRepository, times(1)).findByExperienceId(null);
    }
}
