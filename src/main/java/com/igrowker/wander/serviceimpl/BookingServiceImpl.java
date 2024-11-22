package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.dto.booking.RequestBookingDto;
import com.igrowker.wander.dto.booking.ResponseBookingDto;
import com.igrowker.wander.entity.BookingEntity;
import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.enums.BookingStatus;
import com.igrowker.wander.repository.BookingRepository;
import com.igrowker.wander.repository.ExperienceRepository;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

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
        booking.setPaymentStatus("PENDING");

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