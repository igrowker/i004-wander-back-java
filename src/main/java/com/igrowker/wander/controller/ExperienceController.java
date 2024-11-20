package com.igrowker.wander.controller;

import com.igrowker.wander.dto.experience.RequestExperienceDto;
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

	// Service injection to manage business logic
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

	// Endpoint to list experiences
	@GetMapping
	public ResponseEntity<List<ExperienceEntity>> getExperiences(@RequestParam(required = false) String location,
			@RequestParam(required = false) Double maxPrice) {
		try {
			List<ExperienceEntity> experiences = experienceService.getExperiences(location, maxPrice);
			return ResponseEntity.ok(experiences);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// Endpoint to get experience by ID
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

	// Endpoint to update an experience by ID
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
}
