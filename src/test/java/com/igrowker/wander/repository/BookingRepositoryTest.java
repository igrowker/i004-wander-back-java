package com.igrowker.wander.repository;

import com.igrowker.wander.dto.experience.ExperienceReservationCountDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    public void testFindTopReservedExperiences() {
        // Probar el método
        List<ExperienceReservationCountDto> results = bookingRepository.findTopReservedExperiences(5);

        // Validar resultados
        assertNotNull(results, "El resultado no debe ser nulo");
        assertFalse(results.isEmpty(), "El resultado no debe estar vacío");

        // Imprimir resultados para depuración
        results.forEach(result -> {
            System.out.println("ExperienceId: " + result.getExperienceId() + ", Count: " + result.getCount());
        });
    }
}
