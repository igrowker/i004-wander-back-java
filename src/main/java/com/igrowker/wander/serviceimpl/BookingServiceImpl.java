package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.dto.booking.RequestBookingDto;
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

    @Override
    public ResponseBookingDto updateBooking(String id, RequestBookingDto requestDto) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        if (requestDto.getStatus() != null) {
            booking.setStatus(requestDto.getStatus());
        }

        if (requestDto.getPaymentStatus() != null) {
            booking.setPaymentStatus(requestDto.getPaymentStatus());
        }

        if (requestDto.getBookingDate() != null) {
            booking.setBookingDate(requestDto.getBookingDate());
        }

        if (requestDto.getParticipants() != null) {
            booking.setParticipants(requestDto.getParticipants());
        }

        if (requestDto.getTotalPrice() != null) {
            booking.setTotalPrice(requestDto.getTotalPrice());
        }

        Booking updatedBooking = bookingRepository.save(booking);

        return convertToDto(updatedBooking);
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
