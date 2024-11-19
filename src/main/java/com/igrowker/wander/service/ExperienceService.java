package com.igrowker.wander.service;

import java.util.List;


import com.igrowker.wander.entity.ExperienceEntity;

public interface ExperienceService {
	//Method to create experiences
    ExperienceEntity createExperience(ExperienceEntity experience);
    
    //Method to list experiences with filters
    List<ExperienceEntity> getExperiences(String location, Double maxPrice);

}
