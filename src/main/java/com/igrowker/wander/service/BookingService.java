package com.igrowker.wander.service;

import com.igrowker.wander.dto.booking.RequestBookingDto;
import com.igrowker.wander.dto.booking.ResponseBookingDto;

import java.util.List;

public interface BookingService {
    ResponseBookingDto getBookingById(String id);
    List<ResponseBookingDto> getBookingsByUserId(String userId);
    List<ResponseBookingDto> getBookingsByExperienceId(String experienceId);
    ResponseBookingDto updateBooking(String id, RequestBookingDto requestDto);

}
