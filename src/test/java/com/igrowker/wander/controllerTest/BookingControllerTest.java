package com.igrowker.wander.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igrowker.wander.controller.BookingController;
import com.igrowker.wander.dto.booking.RequestBookingDto;
import com.igrowker.wander.dto.booking.RequestUpdateBookingDto;
import com.igrowker.wander.dto.booking.ResponseBookingDto;
import com.igrowker.wander.service.BookingService;
import com.igrowker.wander.service.ExperienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookingControllerTest {
    @Mock
    private BookingService bookingService;

    @Mock
    private ExperienceService experienceService;

    @InjectMocks
    private BookingController bookingController;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetBookingById() throws Exception {
        String bookingId = "123";
        ResponseBookingDto bookingDto = new ResponseBookingDto();
        bookingDto.setId(bookingId);

        when(bookingService.getBookingById(bookingId)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{id}", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId));

        verify(bookingService, times(1)).getBookingById(bookingId);
    }

    @Test
    void testGetBookingsByUserId() throws Exception {
        String userId = "user123";
        List<ResponseBookingDto> bookings = Arrays.asList(
                new ResponseBookingDto("1", null, null, null, null),
                new ResponseBookingDto("2", null, null, null, null)
        );

        when(bookingService.getBookingsByUserId(userId)).thenReturn(bookings);

        mockMvc.perform(get("/bookings/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(bookings.size()));

        verify(bookingService, times(1)).getBookingsByUserId(userId);
    }

    @Test
    void testGetBookingsByExperienceId() throws Exception {
        String experienceId = "exp123";
        List<ResponseBookingDto> bookings = Arrays.asList(
                new ResponseBookingDto("1", null, null, null, null),
                new ResponseBookingDto("2", null, null, null, null)
        );

        when(bookingService.getBookingsByExperienceId(experienceId)).thenReturn(bookings);

        mockMvc.perform(get("/bookings/experience/{experienceId}", experienceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(bookings.size()));

        verify(bookingService, times(1)).getBookingsByExperienceId(experienceId);
    }

    /*@Test
    void testUpdateBooking() throws Exception {
        String bookingId = "123";
        RequestUpdateBookingDto updateDto = new RequestUpdateBookingDto();
        updateDto.setDetails("Updated details");

        ResponseBookingDto updatedBooking = new ResponseBookingDto();
        updatedBooking.setId(bookingId);
        updatedBooking.setDetails("Updated details");

        when(bookingService.updateBooking(eq(bookingId), any(RequestUpdateBookingDto.class)))
                .thenReturn(updatedBooking);

        mockMvc.perform(put("/bookings/{id}", bookingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingId))
                .andExpect(jsonPath("$.details").value("Updated details")); // Asegúrate de que "details" esté en el JSON

        verify(bookingService, times(1)).updateBooking(eq(bookingId), any(RequestUpdateBookingDto.class));
    }*/

    @Test
    void testCreateBooking() throws Exception {
        RequestBookingDto requestBookingDto = new RequestBookingDto();
        requestBookingDto.setExperienceId("exp123");

        ResponseBookingDto responseBookingDto = new ResponseBookingDto();
        responseBookingDto.setId("123");

        when(bookingService.createBooking(any(RequestBookingDto.class))).thenReturn(responseBookingDto);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBookingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("123"));

        verify(bookingService, times(1)).createBooking(any(RequestBookingDto.class));
    }
}
