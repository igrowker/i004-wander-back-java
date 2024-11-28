package com.igrowker.wander.controller;

import com.igrowker.wander.dto.booking.RequestBookingDto;
import com.igrowker.wander.dto.booking.RequestUpdateBookingDto;
import com.igrowker.wander.dto.booking.ResponseBookingDto;
import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.service.BookingService;
import com.igrowker.wander.service.ExperienceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ExperienceService experienceService;

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

    /**
     * Updates the booking details for a specific booking id
     *
     * @param id booking identifier
     * @param updateDto the DTO containing the updated booking details
     * @return a ResponseEntity containing the updated booking information
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResponseBookingDto> updateBooking(
            @PathVariable String id,
            @RequestBody @Valid RequestUpdateBookingDto updateDto) {

        ResponseBookingDto updatedBooking = bookingService.updateBooking(id, updateDto);
        return ResponseEntity.ok(updatedBooking);
    }


    /**
     * Creates a new booking
     *
     * @param requestBookingDto the DTO containing a new booking
     * @return a ResponseEntity containing the new booking created
     */


    @PostMapping
    public ResponseEntity<ResponseBookingDto> createBooking(@Valid @RequestBody RequestBookingDto requestBookingDto) {
        ResponseBookingDto responseBookingDto = bookingService.createBooking(requestBookingDto);
        return new ResponseEntity<>(responseBookingDto, HttpStatus.CREATED);
    }

}
