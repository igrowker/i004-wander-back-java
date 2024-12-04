package com.igrowker.wander.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.igrowker.wander.dto.experience.ExperienceReservationCountDto;
import com.igrowker.wander.dto.experience.RequestExperienceDto;
import com.igrowker.wander.dto.experience.ResponseExperienceDto;
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

        return experienceRepository.save(experienceEntity);
    }

    @Override
    public List<ExperienceEntity> getExperiences(List<String> location, Double maxPrice, String title, List<String> tags) {
        String country = (location != null && location.size() > 0) ? location.get(0).trim() : null;
        String city = (location != null && location.size() > 1) ? location.get(1).trim() : null;

        if (tags != null && !tags.isEmpty()) {
            if (city != null) {
                return experienceRepository.findByCityAndTagsIn(city, tags);
            }
            if (country != null) {
                return experienceRepository.findByCountryAndTagsIn(country, tags);
            }
            if (location != null) {
                return experienceRepository.findByLocationContainsAndTagsIn(location, tags);
            }
            return experienceRepository.findByTagsIn(tags);
        }

        if (city != null) {
            if (maxPrice != null && title != null) {
                return experienceRepository.findByCityAndPriceLessThanEqualAndTitleContaining(city, maxPrice, title);
            }
            if (title != null) {
                return experienceRepository.findByCityAndTitleContaining(city, title);
            }
            if (maxPrice != null) {
                return experienceRepository.findByCityAndPriceLessThanEqual(city, maxPrice);
            }
            return experienceRepository.findByCity(city);
        }

        if (country != null) {
            if (maxPrice != null && title != null) {
                return experienceRepository.findByCountryAndPriceLessThanEqualAndTitleContaining(country, maxPrice, title);
            }
            if (title != null) {
                return experienceRepository.findByCountryAndTitleContaining(country, title);
            }
            if (maxPrice != null) {
                return experienceRepository.findByCountryAndPriceLessThanEqual(country, maxPrice);
            }
            return experienceRepository.findByCountry(country);
        }

        if (location != null) {
            if (maxPrice != null && title != null) {
                return experienceRepository.findByLocationContainsAndPriceLessThanEqualAndTitleContaining(location, maxPrice, title);
            }
            if (title != null) {
                return experienceRepository.findByLocationContainsAndTitleContaining(location, title);
            }
            if (maxPrice != null) {
                return experienceRepository.findByLocationContainsAndPriceLessThanEqual(location, maxPrice);
            }
            return experienceRepository.findByLocationContains(location);
        }

        if (maxPrice != null && title != null) {
            return experienceRepository.findByPriceLessThanEqualAndTitleContaining(maxPrice, title);
        }
        if (maxPrice != null) {
            return experienceRepository.findByPriceLessThanEqual(maxPrice);
        }
        if (title != null) {
            return experienceRepository.findByTitleContaining(title);
        }

        return experienceRepository.findAll();
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
                .orElseThrow(() -> new IllegalArgumentException("Experience with ID: " + id + " was not found."));
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
                .orElseThrow(() -> new ResourceNotFoundException("Host with id: " + hostId + " not found"));

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
