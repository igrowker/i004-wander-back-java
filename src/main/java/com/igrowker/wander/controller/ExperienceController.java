package com.igrowker.wander.controller;

import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.repository.ExperienceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/experiences")
public class ExperienceController {

	// Repository injection to access the database
    @Autowired
    private ExperienceRepository experienceRepository;

    //Endpoint to create a new experience
    @PostMapping
    public ResponseEntity<String> createExperience(@Valid @RequestBody ExperienceEntity experience) {
        try {
        	// Save the experience in MongoDB
            experienceRepository.save(experience);
            return ResponseEntity.status(HttpStatus.CREATED).body("Experiencia creada con éxito");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la experiencia: " + e.getMessage());
        }
    }
}
