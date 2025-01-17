package com.igrowker.wander.serviceImplTest;

import com.igrowker.wander.dto.booking.RequestBookingDto;
import com.igrowker.wander.dto.booking.RequestUpdateBookingDto;
import com.igrowker.wander.dto.booking.ResponseBookingDto;
import com.igrowker.wander.entity.BookingEntity;
import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.entity.enums.BookingStatus;
import com.igrowker.wander.exception.InvalidUserCredentialsException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.BookingRepository;
import com.igrowker.wander.repository.ExperienceRepository;
import com.igrowker.wander.repository.UserRepository;

import com.igrowker.wander.serviceimpl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceImplTest {
    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBookingById_Success() {
        String bookingId = "booking123";
        String experienceId = "exp123";

        BookingEntity booking = new BookingEntity();
        booking.setId(bookingId);
        booking.setExperienceId(experienceId);

        ExperienceEntity experience = new ExperienceEntity();
        experience.setId(experienceId);
        experience.setTitle("Test Experience");

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(experienceRepository.findById(experienceId)).thenReturn(Optional.of(experience));

        ResponseBookingDto result = bookingService.getBookingById(bookingId);

        assertNotNull(result);
        assertEquals(bookingId, result.getId());
        assertEquals(experienceId, result.getExperienceId());
        assertEquals("Test Experience", result.getExperienceTitle());

        verify(bookingRepository, times(1)).findById(bookingId);
        verify(experienceRepository, times(1)).findById(experienceId);
    }

    @Test
    void testGetBookingById_NotFound() {
        String bookingId = "nonexistent";

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookingService.getBookingById(bookingId));
        verify(bookingRepository, times(1)).findById(bookingId);
    }

    /*@Test
    void testCreateBooking_Success() {
        String userId = "user123";
        String experienceId = "exp456";
        LocalDateTime bookingDate = LocalDateTime.now();

        ExperienceEntity experience = new ExperienceEntity();
        experience.setId(experienceId);
        experience.setPrice(50.0);
        experience.setCapacity(10);
        experience.setAvailabilityDates(Collections.singletonList(String.valueOf(bookingDate))); // Usar LocalDateTime directamente

        User user = new User();
        user.setId(userId);

        RequestBookingDto request = new RequestBookingDto();
        request.setUserId(userId);
        request.setExperienceId(experienceId);
        request.setBookingDate(bookingDate);
        request.setParticipants(2); // Menor que la capacidad

        BookingEntity savedBooking = new BookingEntity();
        savedBooking.setId("booking123");
        savedBooking.setTotalPrice(100.0);

        // Configuración de mocks
        when(experienceRepository.findById(experienceId)).thenReturn(Optional.of(experience));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(savedBooking);


        ResponseBookingDto result = bookingService.createBooking(request);

        // Verificaciones
        assertNotNull(result);
        assertEquals("booking123", result.getId());
        assertEquals(100.0, result.getTotalPrice());
        verify(experienceRepository, times(1)).findById(experienceId);
        verify(userRepository, times(1)).findById(userId);
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }*/

    @Test
    void testUpdateBooking_TouristCancel_Success() {
        String bookingId = "booking123";
        String userId = "user123";
        String experienceId = "exp123";

        BookingEntity booking = new BookingEntity();
        booking.setId(bookingId);
        booking.setExperienceId(experienceId);
        booking.setStatus(BookingStatus.PENDING);

        ExperienceEntity experience = new ExperienceEntity();
        experience.setId(experienceId);
        experience.setTitle("Titulo de experiencia de prueba");

        User user = new User();
        user.setId(userId);
        user.setRole("TOURIST");

        RequestUpdateBookingDto request = new RequestUpdateBookingDto();
        request.setUserId(userId);
        request.setStatus(BookingStatus.CANCELLED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(experienceRepository.findById(experienceId)).thenReturn(Optional.of(experience));
        when(bookingRepository.save(any(BookingEntity.class))).thenReturn(booking);

        ResponseBookingDto result = bookingService.updateBooking(bookingId, request);

        assertNotNull(result);
        assertEquals(BookingStatus.CANCELLED, result.getStatus());
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(userRepository, times(1)).findById(userId);
        verify(experienceRepository, times(1)).findById(experienceId);
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    void testUpdateBooking_InvalidRole() {
        String bookingId = "booking123";
        String userId = "user123";

        BookingEntity booking = new BookingEntity();
        booking.setId(bookingId);

        User user = new User();
        user.setId(userId);
        user.setRole("UNAUTHORIZED");

        RequestUpdateBookingDto request = new RequestUpdateBookingDto();
        request.setUserId(userId);
        request.setStatus(BookingStatus.CANCELLED);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(InvalidUserCredentialsException.class, () -> bookingService.updateBooking(bookingId, request));
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(userRepository, times(1)).findById(userId);
    }
}
