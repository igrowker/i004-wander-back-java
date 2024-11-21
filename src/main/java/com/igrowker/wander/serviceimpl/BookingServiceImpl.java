package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.entity.Booking;
import com.igrowker.wander.repository.BookingRepository;
import com.igrowker.wander.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking getBookingById(String id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + id));
    }

    public List<Booking> getBookingsByUserId(String userId) {
        return bookingRepository.findByUserId(userId);
    }

    public List<Booking> getBookingsByExperienceId(String experienceId) {
        return bookingRepository.findByExperienceId(experienceId);
    }

}
