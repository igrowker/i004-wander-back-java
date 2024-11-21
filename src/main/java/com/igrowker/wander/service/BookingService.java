package com.igrowker.wander.service;

import com.igrowker.wander.entity.Booking;

import java.util.List;

public interface BookingService {
    Booking getBookingById(String id);
    List<Booking> getBookingsByUserId(String userId);
    List<Booking> getBookingsByExperienceId(String experienceId);

}
