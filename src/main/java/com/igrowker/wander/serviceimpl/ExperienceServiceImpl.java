package com.igrowker.wander.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.igrowker.wander.dto.experience.RequestExperienceDto;
import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.exception.InvalidDataException;
import com.igrowker.wander.repository.ExperienceRepository;
import com.igrowker.wander.service.ExperienceService;

@Service
public class ExperienceServiceImpl implements ExperienceService {

	@Autowired
	private ExperienceRepository experienceRepository;

	@Override 
	// Method to create a new experience
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
	// Method to get a list of experiences with optional filters
    public List<ExperienceEntity> getExperiences(String location, Double maxPrice) {
        // Check which filters are present and create the corresponding query
        if (location != null && maxPrice != null) {
            return experienceRepository.findByLocationAndPriceLessThanEqual(location, maxPrice);
        } else if (location != null) {
            return experienceRepository.findByLocation(location);
        } else if (maxPrice != null) {
            return experienceRepository.findByPriceLessThanEqual(maxPrice);
        } else {
            // If no filters are provided, we return all experiences
            return experienceRepository.findAll();
        }
    }
	
	@Override
	// Method to get a single experience by ID
	public ExperienceEntity getExperienceById(String id) {
	    /* Use the repository to find the experience by ID
	    If not found, throw an IllegalArgumentException with an appropriate message*/
	    return experienceRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Experience with ID: " + id + " was not found."));
	}

	@Override
	//Method to update an experience by provider
	public ExperienceEntity updateExperience(String id, ExperienceEntity newExperienceData) {
	    // Step 1: Find the existing experience by ID
	    ExperienceEntity existingExperience = experienceRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("No se encontró la experiencia con el ID: " + id));

	    // Step 2: Validate if the user is the creator of the experience
	    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    if (!existingExperience.getHostId().equals(user.getId())) {
	        throw new SecurityException("El usuario no tiene permiso para editar esta experiencia.");
	    }

	    // Step 3: Update the experience fields with the new data
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

	    // Step 4: Save the updated experience to the database
	    return experienceRepository.save(existingExperience);
	}

}
