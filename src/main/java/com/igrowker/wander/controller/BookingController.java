package com.igrowker.wander.controller;

import com.igrowker.wander.dto.booking.ResponseBookingDto;
import com.igrowker.wander.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * get details of a booking by id
     *
     * @param id booking identifier
     * @return Booking details as a ResponseBookingDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResponseBookingDto> getBookingById(@PathVariable String id) {
        ResponseBookingDto bookingDto = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingDto);
    }

    /**
     * Get all user bookings.
     *
     * @param userId user identifier.
     * @return List of ResponseBookingDto representing the bookings
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResponseBookingDto>> getBookingsByUserId(@PathVariable String userId) {
        List<ResponseBookingDto> bookingDtos = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.ok(bookingDtos);
    }

    /**
     * Get all bookings of an experience
     *
     * @param experienceId experience id
     * @return List of ResponseBookingDto representing the bookings
     */
    @GetMapping("/experience/{experienceId}")
    public ResponseEntity<List<ResponseBookingDto>> getBookingsByExperienceId(@PathVariable String experienceId) {
        List<ResponseBookingDto> bookingDtos = bookingService.getBookingsByExperienceId(experienceId);
        return ResponseEntity.ok(bookingDtos);
    }


}
