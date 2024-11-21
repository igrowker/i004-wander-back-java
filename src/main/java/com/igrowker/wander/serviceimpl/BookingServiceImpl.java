package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.dto.booking.ResponseBookingDto;
import com.igrowker.wander.entity.Booking;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.BookingRepository;
import com.igrowker.wander.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public ResponseBookingDto getBookingById(String id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        return convertToDto(booking);
    }

    @Override
    public List<ResponseBookingDto> getBookingsByUserId(String userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        return bookings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<ResponseBookingDto> getBookingsByExperienceId(String experienceId) {
        List<Booking> bookings = bookingRepository.findByExperienceId(experienceId);
        return bookings.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private ResponseBookingDto convertToDto(Booking booking) {
        return new ResponseBookingDto(
                booking.getId(),
                booking.getExperienceId(),
                booking.getUserId(),
                booking.getStatus(),
                booking.getBookingDate(),
                booking.getTotalPrice(),
                booking.getParticipants(),
                booking.getPaymentStatus(),
                booking.getCreatedAt()
        );
    }

}
