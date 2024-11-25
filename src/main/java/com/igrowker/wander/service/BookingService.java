package com.igrowker.wander.service;

import com.igrowker.wander.dto.booking.RequestBookingDto;
import com.igrowker.wander.dto.booking.ResponseBookingDto;

import java.util.List;

public interface BookingService {
    ResponseBookingDto createBooking(RequestBookingDto requestBookingDto);

}
