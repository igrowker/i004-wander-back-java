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
	        @RequestParam(required = false) List<String> location,
	        @RequestParam(required = false) Double maxPrice,
	        @RequestParam(required = false) String title) {
	    try {
	        String city = (location != null && location.size() > 1) ? location.get(location.size() - 2) : null;
	        String country = (location != null && location.size() > 2) ? location.get(location.size() - 1) : null;

	        List<ExperienceEntity> experiences = experienceService.getExperiences(location, maxPrice, title);
	        return ResponseEntity.ok(experiences);
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
	
	@GetMapping("/tags/{tag}")
	public ResponseEntity<List<ExperienceEntity>> getExperiencesByTag(@PathVariable String tag) {
	    List<ExperienceEntity> experiences = experienceService.getExperiencesByTag(tag);
	    return ResponseEntity.ok(experiences);
	}
	
	@GetMapping("/tags")
	public ResponseEntity<List<ExperienceEntity>> getExperiencesByMultipleTags(
	        @RequestParam List<String> tags) {
	    try {
	        List<ExperienceEntity> experiences = experienceService.getExperiencesByMultipleTags(tags);
	        return ResponseEntity.ok(experiences);
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
}
