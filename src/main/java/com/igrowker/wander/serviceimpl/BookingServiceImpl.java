package com.igrowker.wander.serviceimpl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.igrowker.wander.service.UserService;
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
    private final UserService userService;

    public BookingServiceImpl(BookingRepository bookingRepository, ExperienceRepository experienceRepository, UserRepository userRepository, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.experienceRepository = experienceRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public ResponseBookingDto getBookingById(String id) {
        BookingEntity booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la reserva con ID: " + id));
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
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario"));
        validateUserRoleAndUpdateBooking(user, requestDto, booking);
        BookingEntity updatedBooking = bookingRepository.save(booking);
        return convertToResponseDto(updatedBooking);
    }

    @Override
    public ResponseBookingDto createBooking(RequestBookingDto requestBookingDto) {
        ExperienceEntity experience = experienceRepository.findById(requestBookingDto.getExperienceId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la experiencia"));

        User tourist = userRepository.findById(requestBookingDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el turista"));

        User provider = userRepository.findById(experience.getHostId())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el proveedor"));

        if (!isExperienceAvailable(experience, requestBookingDto.getBookingDate(), requestBookingDto.getParticipants())) {
            throw new IllegalArgumentException("La experiencia no está disponible para la fecha seleccionada o el número de participantes.");
        }

        BookingEntity booking = new BookingEntity();
        booking.setExperienceId(experience.getId());
        booking.setUserId(tourist.getId());
        booking.setTouristInfo(userService.convertToUserDto(tourist));
        booking.setProviderInfo(userService.convertToUserDto(provider));
        booking.setBookingDate(requestBookingDto.getBookingDate());
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
                throw new IllegalArgumentException("Los turistas solo pueden cancelar reservas.");
            }
            booking.setStatus(BookingStatus.CANCELLED);
        } else if ("PROVIDER".equalsIgnoreCase(user.getRole())) {
            if (!isProviderStatusValid(requestDto.getStatus())) {
                throw new IllegalArgumentException("Estado inválido para el proveedor.");
            }
            booking.setStatus(requestDto.getStatus());
        } else {
            throw new InvalidUserCredentialsException("Usuario no autorizado para actualizar la reserva.");
        }
    }

    private boolean isExperienceAvailable(ExperienceEntity experience, Instant bookingDate, int participants) {

        boolean isDateAvailable = experience.getAvailabilityDates().stream()
                .map(Instant::parse)
                .anyMatch(date -> date.equals(bookingDate));

        if (!isDateAvailable) {
            return false;
        }

        List<BookingEntity> existingBookings = bookingRepository.findByExperienceIdAndBookingDate(
                experience.getId(), bookingDate);

        System.out.println(existingBookings);

        int currentCapacity = existingBookings.stream()
                .mapToInt(BookingEntity::getParticipants)
                .sum();

        System.out.println(currentCapacity);

        return (experience.getCapacity() - currentCapacity) >= participants;
    }

    private double calculateTotalPrice(ExperienceEntity experience, int participants) {
        return experience.getPrice() * participants;
    }

    private boolean isProviderStatusValid(BookingStatus status) {
        return status == BookingStatus.CANCELLED || status == BookingStatus.CONFIRMED || status == BookingStatus.PENDING;
    }

    private ResponseBookingDto convertToResponseDto(BookingEntity booking) {

        ExperienceEntity experience = experienceRepository.findById(booking.getExperienceId())
                .orElseThrow(() -> new ResourceNotFoundException("Esperiencia no encontrada"));

        return ResponseBookingDto.builder()
                .id(booking.getId())
                .experienceId(booking.getExperienceId())
                .experienceTitle(experience.getTitle())
                .experienceImages(experience.getExperienceImages())
                .userId(booking.getUserId())
                .tourist(booking.getTouristInfo())
                .provider(booking.getProviderInfo())
                .status(booking.getStatus())
                .bookingDate(booking.getBookingDate())
                .totalPrice(booking.getTotalPrice())
                .participants(booking.getParticipants())
                .paymentStatus(booking.getPaymentStatus())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}