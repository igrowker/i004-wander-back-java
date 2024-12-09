package com.igrowker.wander.controller;

import com.igrowker.wander.dto.experience.RequestExperienceDto;
import com.igrowker.wander.dto.experience.ResponseExperienceDto;
import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.User;
import com.igrowker.wander.service.ExperienceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/experiences")
public class ExperienceController {

	@Autowired
	private ExperienceService experienceService;

	/**
	 * Endpoint to create a new experience
	 * 
	 * @param experience Object received from the BFF with the experience data
	 * @return Response indicating if the experience was created successfully
	 */
	@PostMapping
	public ResponseEntity<ExperienceEntity> createExperience(@Valid @RequestBody RequestExperienceDto experience,
			Authentication authentication) {
		User user = (User) authentication.getPrincipal();

		ExperienceEntity savedexperience = experienceService.createExperience(experience, user);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedexperience);

	}

	@GetMapping
	public ResponseEntity<List<ExperienceEntity>> getExperiences(
	        @RequestParam(required = false) String location,
	        @RequestParam(required = false) Double maxPrice,
	        @RequestParam(required = false) String title,
	        @RequestParam(required = false) List<String> tags) {
	    try {
	        List<String> locationParts = (location != null && !location.trim().isEmpty())
	                ? List.of(location.split(","))
	                : null;
	        List<ExperienceEntity> experiences = experienceService.getExperiences(locationParts, maxPrice, title, tags);
	        return ResponseEntity.ok(experiences);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}



	@GetMapping("/{id}")
	public ResponseEntity<ExperienceEntity> getExperienceById(@PathVariable String id) {
		try {
			ExperienceEntity experience = experienceService.getExperienceById(id);
			return ResponseEntity.ok(experience);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ExperienceEntity> updateExperience(@PathVariable String id,
			@Valid @RequestBody ExperienceEntity newExperienceData) {
		try {
			ExperienceEntity updatedExperience = experienceService.updateExperience(id, newExperienceData);
			return ResponseEntity.ok(updatedExperience);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (SecurityException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/latest")
	public ResponseEntity<List<ExperienceEntity>> getLatestExperiences(
			@RequestParam(defaultValue = "5") int limit) {
		try {
			List<ExperienceEntity> latestExperiences = experienceService.getLatestExperiences(limit);
			return ResponseEntity.ok(latestExperiences);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/top-rated")
	public ResponseEntity<List<ExperienceEntity>> getTopRatedExperiences(
			@RequestParam(defaultValue = "5") int limit) {
		try {
			List<ExperienceEntity> topRatedExperiences = experienceService.getTopRatedExperiences(limit);
			return ResponseEntity.ok(topRatedExperiences);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/most-reserved")
	public ResponseEntity<List<ExperienceEntity>> getMostReservedExperiences(
			@RequestParam(defaultValue = "5") int limit) {
		try {
			List<ExperienceEntity> experiences = experienceService.getMostReservedExperiences(limit);
			return ResponseEntity.ok(experiences);
		} catch (Exception e) {
			// Log de la excepción
			e.printStackTrace(); // Para que aparezca en la consola
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * Retrieves all experiences for a specific host
	 *
	 * @param hostId the identifier of the host
	 * @return List of ResponseExperienceDto representing the experiences associated with the specified host
	 */
	@GetMapping("/host/{hostId}")
	public ResponseEntity<List<ResponseExperienceDto>> getExperiencesByHost(@PathVariable String hostId) {
		List<ResponseExperienceDto> responseDtos = experienceService.getExperiencesByHost(hostId);
		return ResponseEntity.ok(responseDtos);
	}
}
