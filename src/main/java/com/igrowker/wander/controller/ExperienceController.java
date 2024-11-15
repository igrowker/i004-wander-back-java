package com.igrowker.wander.controller;

import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.service.ExperienceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/experiences")
public class ExperienceController {

    // Service injection to manage business logic
    @Autowired
    private ExperienceService experienceService;

    /**
     * Endpoint to create a new experience
     * @param experience Object received from the BFF with the experience data
     * @return Response indicating if the experience was created successfully
     */
    @PostMapping
    public ResponseEntity<String> createExperience(@Valid @RequestBody ExperienceEntity experience) {
        try {
            // Delegate the creation of experience to the service layer
            experienceService.createExperience(experience);
            return ResponseEntity.status(HttpStatus.CREATED).body("Experiencia creada con éxito");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error en los datos de la experiencia: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al crear la experiencia: " + e.getMessage());
        }
    }
}
