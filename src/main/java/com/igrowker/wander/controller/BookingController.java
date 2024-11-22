package com.igrowker.wander.controller;

import com.igrowker.wander.dto.booking.RequestBookingDto;
import com.igrowker.wander.dto.booking.ResponseBookingDto;
import com.igrowker.wander.service.BookingService;
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

    @PostMapping
    public ResponseEntity<ResponseBookingDto> createBooking(@Valid @RequestBody RequestBookingDto requestBookingDto) {
        ResponseBookingDto responseBookingDto = bookingService.createBooking(requestBookingDto);
        return new ResponseEntity<>(responseBookingDto, HttpStatus.CREATED);
    }
}
