package com.igrowker.wander.service;

import com.igrowker.wander.dto.booking.RequestBookingDto;
import com.igrowker.wander.dto.booking.RequestUpdateBookingDto;
import com.igrowker.wander.dto.booking.ResponseBookingDto;
import com.igrowker.wander.entity.ExperienceEntity;

import java.util.List;

public interface BookingService {

    ResponseBookingDto getBookingById(String id);
    List<ResponseBookingDto> getBookingsByUserId(String userId);
    List<ResponseBookingDto> getBookingsByExperienceId(String experienceId);
    ResponseBookingDto updateBooking(String id, RequestUpdateBookingDto requestDto);
    ResponseBookingDto createBooking(RequestBookingDto requestBookingDto);

}
