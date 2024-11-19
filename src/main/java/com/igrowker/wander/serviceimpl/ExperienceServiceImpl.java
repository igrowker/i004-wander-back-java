package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.repository.ExperienceRepository;
import com.igrowker.wander.service.ExperienceService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ExperienceServiceImpl implements ExperienceService {

	@Autowired
	private ExperienceRepository experienceRepository;

	@Override
	public ExperienceEntity createExperience(ExperienceEntity experience) {
		// Retrieve authentication from the security context
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// Ensure the authentication is not null and the context has a valid user
		if (authentication == null || !authentication.isAuthenticated()) {
			throw new SecurityException("El usuario no está autenticado.");
		}

		// Retrieve the current user from the authentication
		User user = (User) authentication.getPrincipal();

		// Ensure the user has the "PROVIDER" role before proceeding
		if (!user.getRole().equalsIgnoreCase("PROVIDER")) {
			throw new SecurityException("El usuario no tiene permisos para crear una experiencia.");
		}

		// Assign the host ID from the authenticated user
		experience.setHostId(user.getId());

		// Additional validations for the experience data
		// Title validation
		if (experience.getTitle() == null || experience.getTitle().trim().isEmpty()) {
			throw new IllegalArgumentException("El título de la experiencia es obligatorio y no puede estar vacío.");
		}
		if (experience.getTitle().length() < 3 || experience.getTitle().length() > 100) {
			throw new IllegalArgumentException("El título debe tener entre 3 y 100 caracteres.");
		}

		// Description validation
		if (experience.getDescription() == null || experience.getDescription().trim().isEmpty()) {
			throw new IllegalArgumentException(
					"La descripción de la experiencia es obligatoria y no puede estar vacía.");
		}
		if (experience.getDescription().length() < 10 || experience.getDescription().length() > 5000) {
			throw new IllegalArgumentException("La descripción debe tener entre 10 y 5000 caracteres.");
		}

		// Location validation
		if (experience.getLocation() == null || experience.getLocation().trim().isEmpty()) {
			throw new IllegalArgumentException("La ubicación de la experiencia es obligatoria.");
		}

		// Price validation
		if (experience.getPrice() <= 0) {
			throw new IllegalArgumentException("El precio debe ser un valor positivo.");
		}

		/*
		 * Capacity validation is necessary? if (experience.getCapacity() < 1) { throw
		 * new IllegalArgumentException("La capacidad debe ser al menos 1."); }
		 */

		// Save the experience in the database after validation
		return experienceRepository.save(experience);
	}

	@Override
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
	public ExperienceEntity getExperienceById(String id) {
	    /* Use the repository to find the experience by ID
	    If not found, throw an IllegalArgumentException with an appropriate message*/
	    return experienceRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("Experience with ID: " + id + " was not found."));
	}

}
