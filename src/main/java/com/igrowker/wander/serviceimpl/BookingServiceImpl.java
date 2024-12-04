package com.igrowker.wander.serviceimpl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    private final BookingRepository bookingRepository;
    private final ExperienceRepository experienceRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              ExperienceRepository experienceRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.experienceRepository = experienceRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseBookingDto getBookingById(String id) {
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        return convertToResponseDto(booking);
    }

    @Override
    public List<ResponseBookingDto> getBookingsByUserId(String userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResponseBookingDto> getBookingsByExperienceId(String experienceId) {
        return bookingRepository.findByExperienceId(experienceId)
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseBookingDto updateBooking(String id, RequestUpdateBookingDto requestDto) {
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validateUserRoleAndUpdateBooking(user, requestDto, booking);

        BookingEntity updatedBooking = bookingRepository.save(booking);
        return convertToResponseDto(updatedBooking);
    }

    @Override
    public ResponseBookingDto createBooking(RequestBookingDto requestBookingDto) {
        ExperienceEntity experience = experienceRepository.findById(requestBookingDto.getExperienceId())
                .orElseThrow(() -> new ResourceNotFoundException("Experience not found"));

        User user = userRepository.findById(requestBookingDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!isExperienceAvailable(experience, requestBookingDto.getBookingDate(), requestBookingDto.getParticipants())) {
            throw new IllegalArgumentException("Experience is not available for the selected date or number of participants");
        }

        BookingEntity booking = new BookingEntity();
        booking.setExperienceId(experience.getId());
        booking.setUserId(user.getId());
        booking.setBookingDate(convertToLocalDateTime(requestBookingDto.getBookingDate()));
        booking.setParticipants(requestBookingDto.getParticipants());
        booking.setTotalPrice(calculateTotalPrice(experience, requestBookingDto.getParticipants()));
        booking.setStatus(BookingStatus.PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);

        BookingEntity savedBooking = bookingRepository.save(booking);
        return convertToResponseDto(savedBooking);
    }

    private void validateUserRoleAndUpdateBooking(User user, RequestUpdateBookingDto requestDto, BookingEntity booking) {
        if ("TOURIST".equalsIgnoreCase(user.getRole())) {
            if (requestDto.getStatus() != BookingStatus.CANCELLED) {
                throw new IllegalArgumentException("Tourists can only cancel bookings.");
            }
            booking.setStatus(BookingStatus.CANCELLED);
        } else if ("PROVIDER".equalsIgnoreCase(user.getRole())) {
            if (!isProviderStatusValid(requestDto.getStatus())) {
                throw new IllegalArgumentException("Invalid status for provider.");
            }
            booking.setStatus(requestDto.getStatus());
        } else {
            throw new InvalidUserCredentialsException("User not authorized to update booking.");
        }
    }

    private boolean isExperienceAvailable(ExperienceEntity experience, Date bookingDate, int participants) {
        LocalDateTime bookingDateTime = convertToLocalDateTime(bookingDate);
        boolean isDateAvailable = experience.getAvailabilityDates().stream()
                .anyMatch(date -> date.equals(bookingDateTime));
        return isDateAvailable && experience.getCapacity() >= participants;
    }

    private double calculateTotalPrice(ExperienceEntity experience, int participants) {
        return experience.getPrice() * participants;
    }

    private boolean isProviderStatusValid(BookingStatus status) {
        return status == BookingStatus.CANCELLED || status == BookingStatus.CONFIRMED || status == BookingStatus.PENDING;
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

    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}