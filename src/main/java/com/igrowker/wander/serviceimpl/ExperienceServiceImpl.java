package com.igrowker.wander.serviceimpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.igrowker.wander.dto.experience.*;
import com.igrowker.wander.entity.BookingEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.InvalidDataException;
import com.igrowker.wander.exception.ResourceNotFoundException;
import com.igrowker.wander.repository.BookingRepository;
import com.igrowker.wander.repository.ExperienceRepository;
import com.igrowker.wander.repository.UserRepository;
import com.igrowker.wander.service.ExperienceService;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ExperienceEntity createExperience(RequestExperienceDto requestExperienceDto, User user) {
        if (!user.getRole().equalsIgnoreCase("PROVIDER")) {
            throw new InvalidDataException("El usuario no tiene permisos para crear una experiencia.");
        }
        ExperienceEntity experienceEntity = new ExperienceEntity();

        experienceEntity.setHostId(user.getId());
        experienceEntity.setTitle(requestExperienceDto.getTitle());
        experienceEntity.setDescription(requestExperienceDto.getDescription());
        experienceEntity.setLocation(requestExperienceDto.getLocation());
        experienceEntity.setPrice(requestExperienceDto.getPrice());
        experienceEntity.setAvailabilityDates(requestExperienceDto.getAvailabilityDates());
        experienceEntity.setTags(requestExperienceDto.getTags());
        experienceEntity.setCapacity(requestExperienceDto.getCapacity());
        experienceEntity.setExperienceImages(requestExperienceDto.getExperienceImages());

        return experienceRepository.save(experienceEntity);
    }

    @Override
    public List<ExperienceEntity> getExperiences(List<String> location, Double maxPrice, String title, List<String> tags) {
        // Control: Si no hay filtros, devolver todo
        if ((location == null || location.isEmpty()) 
                && maxPrice == null 
                && (title == null || title.trim().isEmpty()) 
                && (tags == null || tags.isEmpty())) {
            return experienceRepository.findAll();
        }

        // Extraer país y ciudad
        String country = (location != null && location.size() > 0 && !location.get(0).trim().isEmpty()) ? location.get(0).trim() : null;
        String city = (location != null && location.size() > 1 && !location.get(1).trim().isEmpty()) ? location.get(1).trim() : null;

        // Lista de resultados para ir acumulando las experiencias
        List<ExperienceEntity> results = new ArrayList<>();

        // Filtro: País y ciudad
        if (country != null && city != null) {
            results = experienceRepository.findByCountryAndCity(country, city);
        } else if (city != null) {
            // Filtro: Solo ciudad
            results = experienceRepository.findByCity(city);
        } else if (country != null) {
            // Filtro: Solo país
            results = experienceRepository.findByCountry(country);
        } else {
            // Filtro: Si solo queda `location` genérico
            results = experienceRepository.findByLocationContains(location);
        }

        // Filtro adicional: Tags
        if (tags != null && !tags.isEmpty()) {
            results = results.stream()
                    .filter(exp -> exp.getTags() != null && !exp.getTags().isEmpty() && exp.getTags().stream().anyMatch(tags::contains))
                    .collect(Collectors.toList());
        }

        // Filtro adicional: Precio máximo
        if (maxPrice != null) {
            results = results.stream()
                    .filter(exp -> exp.getPrice() != null && exp.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        // Filtro adicional: Título
        if (title != null && !title.trim().isEmpty()) {
            results = results.stream()
                    .filter(exp -> exp.getTitle() != null && exp.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return results;
    }


    @Override
    public List<ExperienceEntity> getExperiences(List<String> location, Double maxPrice) {
        String country = (location != null && location.size() > 0) ? location.get(0).trim() : null;
        String city = (location != null && location.size() > 1) ? location.get(1).trim() : null;

        if (city != null && maxPrice != null) {
            return experienceRepository.findByCityAndPriceLessThanEqual(city, maxPrice);
        }
        if (city != null) {
            return experienceRepository.findByCity(city);
        }

        if (country != null && maxPrice != null) {
            return experienceRepository.findByCountryAndPriceLessThanEqual(country, maxPrice);
        }
        if (country != null) {
            return experienceRepository.findByCountry(country);
        }

        if (location != null && maxPrice != null) {
            return experienceRepository.findByLocationContainsAndPriceLessThanEqual(location, maxPrice);
        }
        if (location != null) {
            return experienceRepository.findByLocationContains(location);
        }
        if (maxPrice != null) {
            return experienceRepository.findByPriceLessThanEqual(maxPrice);
        }

        return experienceRepository.findAll();
    }

    @Override
    public ExperienceEntity getExperienceById(String id) {
        return experienceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la experiencia con el ID: " + id));
    }

    @Override
    public ExperienceEntity updateExperience(String id, ExperienceEntity newExperienceData) {
        ExperienceEntity existingExperience = experienceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la experiencia con el ID: " + id));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!existingExperience.getHostId().equals(user.getId())) {
            throw new SecurityException("El usuario no tiene permiso para editar esta experiencia.");
        }

        if (newExperienceData.getTitle() != null) {
            existingExperience.setTitle(newExperienceData.getTitle());
        }
        if (newExperienceData.getDescription() != null) {
            existingExperience.setDescription(newExperienceData.getDescription());
        }
        if (newExperienceData.getLocation() != null) {
            existingExperience.setLocation(newExperienceData.getLocation());
        }
        if (newExperienceData.getPrice() > 0) {
            existingExperience.setPrice(newExperienceData.getPrice());
        }
        if (newExperienceData.getAvailabilityDates() != null) {
            existingExperience.setAvailabilityDates(newExperienceData.getAvailabilityDates());
        }
        if (newExperienceData.getTags() != null) {
            existingExperience.setTags(newExperienceData.getTags());
        }
        if (newExperienceData.getCapacity() > 0 && newExperienceData.getCapacity() <= 50) {
            existingExperience.setCapacity(newExperienceData.getCapacity());
        }

        return experienceRepository.save(existingExperience);
    }

    @Override
    public List<ExperienceEntity> getExperiencesByTag(String tag) {
        return experienceRepository.findByTagsContaining(tag);
    }

    @Override
    public List<ExperienceEntity> getExperiencesByMultipleTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            throw new IllegalArgumentException("La lista de tags no puede estar vacía.");
        }
        return experienceRepository.findByTagsIn(tags);
    }

    @Override
    public List<ResponseExperienceDto> getExperiencesByHost(String hostId) {
        userRepository.findById(hostId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el anfitrión con ID: " + hostId));

        List<ExperienceEntity> experiences = experienceRepository.findByHostId(hostId);

        return experiences.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExperienceEntity> getLatestExperiences(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return experienceRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    @Override
    public List<ExperienceEntity> getTopRatedExperiences(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return experienceRepository.findAllByOrderByRatingDesc(pageable);
    }

    @Override
    public List<ExperienceEntity> getMostReservedExperiences(int limit) {
        List<ExperienceReservationCountDto> topReserved = bookingRepository.findTopReservedExperiences(limit);

        List<String> experienceIds = topReserved.stream()
                .map(ExperienceReservationCountDto::getExperienceId)
                .collect(Collectors.toList());

        return experienceRepository.findAllById(experienceIds);
    }

    public ResponseExperienceWithSlotsDto getExperienceByIdWithSlots(String id) {
        ExperienceEntity experience = experienceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Experiencia no encontrada con el id: " + id));

        List<AvailabilityDateWithSlotsDto> availabilityWithSlots = new ArrayList<>();

        for (String date : experience.getAvailabilityDates()) {
            Instant bookingDate = Instant.parse(date);
            AvailabilityDateWithSlotsDto dto = calculateAvailableSlotsForDate(experience, bookingDate);
            availabilityWithSlots.add(dto);
        }

        return new ResponseExperienceWithSlotsDto(
                experience.getId(),
                experience.getTitle(),
                experience.getDescription(),
                experience.getLocation(),
                experience.getHostId(),
                experience.getPrice(),
                availabilityWithSlots,
                experience.getTags(),
                experience.getRating(),
                experience.getCapacity(),
                experience.getCreatedAt(),
                experience.isStatus()
        );
    }

    private AvailabilityDateWithSlotsDto calculateAvailableSlotsForDate(ExperienceEntity experience, Instant bookingDate) {
        List<BookingEntity> bookingsForDate = bookingRepository.findByExperienceIdAndBookingDate(
                experience.getId(),
                bookingDate
        );

        int totalParticipantsBooked = 0;
        for (BookingEntity booking : bookingsForDate) {
            totalParticipantsBooked += booking.getParticipants();
        }

        int availableSlots = experience.getCapacity() - totalParticipantsBooked;

        return new AvailabilityDateWithSlotsDto(bookingDate.toString(), Math.max(availableSlots, 0));
    }

    private ResponseExperienceDto convertToResponseDto(ExperienceEntity experience) {
        return new ResponseExperienceDto(
                experience.getId(),
                experience.getTitle(),
                experience.getDescription(),
                experience.getLocation(),
                experience.getHostId(),
                experience.getPrice(),
                experience.getAvailabilityDates(),
                experience.getTags(),
                experience.getRating(),
                experience.getCapacity(),
                experience.getCreatedAt(),
                experience.isStatus()
        );
    }
}
