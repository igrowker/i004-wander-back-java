package com.igrowker.wander.serviceimpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.igrowker.wander.dto.booking.RequestBookingDto;
import com.igrowker.wander.dto.booking.RequestUpdateBookingDto;
import com.igrowker.wander.dto.booking.ResponseBookingDto;
import com.igrowker.wander.entity.BookingEntity;
import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.entity.enums.BookingStatus;
import com.igrowker.wander.entity.enums.PaymentStatus;
import com.igrowker.wander.exception.InvalidUserCredentialsException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.BookingRepository;
import com.igrowker.wander.repository.ExperienceRepository;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.service.BookingService;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseBookingDto getBookingById(String id) {
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        return convertToResponseDto(booking);
    }

    @Override
    public List<ResponseBookingDto> getBookingsByUserId(String userId) {
        List<BookingEntity> bookings = bookingRepository.findByUserId(userId);
        return bookings.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    @Override
    public List<ResponseBookingDto> getBookingsByExperienceId(String experienceId) {
        List<BookingEntity> bookings = bookingRepository.findByExperienceId(experienceId);
        return bookings.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    @Override
    public ResponseBookingDto updateBooking(String id, RequestUpdateBookingDto requestDto) {

        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if("TOURIST".equalsIgnoreCase(user.getRole())){
            if (requestDto.getStatus() != BookingStatus.CANCELLED) {
                throw new IllegalArgumentException("Tourists can only cancel bookings.");
            }
            booking.setStatus(BookingStatus.CANCELLED);
        }
        else if ("PROVIDER".equalsIgnoreCase(user.getRole())) {
            if (requestDto.getStatus() == null ||
                    !(requestDto.getStatus() == BookingStatus.CANCELLED ||
                            requestDto.getStatus() == BookingStatus.CONFIRMED ||
                            requestDto.getStatus() == BookingStatus.PENDING)) {
                throw new IllegalArgumentException("Invalid status for provider.");
            }
            booking.setStatus(requestDto.getStatus());
        } else {
            throw new InvalidUserCredentialsException("User not authorized to update booking.");
        }

        BookingEntity updatedBooking = bookingRepository.save(booking);

        return convertToResponseDto(updatedBooking);
    }


    @Override
    public ResponseBookingDto createBooking(RequestBookingDto requestBookingDto) {
        ExperienceEntity experience = experienceRepository.findById(requestBookingDto.getExperienceId())
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        User user = userRepository.findById(requestBookingDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!isExperienceAvailable(experience, requestBookingDto.getBookingDate(), requestBookingDto.getParticipants())) {
            throw new RuntimeException("Experience is not available for the selected date or number of participants");
        }

        BookingEntity booking = new BookingEntity();
        booking.setExperienceId(experience);
        booking.setUserId(user);
        booking.setBookingDate(requestBookingDto.getBookingDate());
        booking.setParticipants(requestBookingDto.getParticipants());
        booking.setTotalPrice(calculateTotalPrice(experience, requestBookingDto.getParticipants()));
        booking.setStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);

        BookingEntity savedBooking = bookingRepository.save(booking);

        return convertToResponseDto(savedBooking);
    }

    private boolean isExperienceAvailable(ExperienceEntity experience, Date bookingDate, int participants) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedDate = sdf.format(bookingDate);

        return experience.getAvailabilityDates().contains(formattedDate) &&
                experience.getCapacity() >= participants;
    }

    private double calculateTotalPrice(ExperienceEntity experience, int participants) {
        return experience.getPrice() * participants;
    }

    private ResponseBookingDto convertToResponseDto(BookingEntity booking) {
        ResponseBookingDto responseDto = new ResponseBookingDto();
        responseDto.setId(booking.getId());
        responseDto.setExperienceId(booking.getExperienceId().getId());
        responseDto.setUserId(booking.getUserId().getId());
        responseDto.setStatus(booking.getStatus());
        responseDto.setBookingDate(booking.getBookingDate());
        responseDto.setTotalPrice(booking.getTotalPrice());
        responseDto.setParticipants(booking.getParticipants());
        responseDto.setPaymentStatus(booking.getPaymentStatus());
        responseDto.setCreatedAt(booking.getCreatedAt());
        return responseDto;
    }
}
