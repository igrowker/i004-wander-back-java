package com.igrowker.wander.service;

import java.util.List;

import com.igrowker.wander.dto.experience.RequestExperienceDto;
import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.entity.User;

public interface ExperienceService {
	ExperienceEntity createExperience(RequestExperienceDto experience, User user);
    
    List<ExperienceEntity> getExperiences(List<String> location, Double maxPrice, String title);
    
    List<ExperienceEntity> getExperiences(List<String> location, Double maxPrice);

    ExperienceEntity getExperienceById(String id);

    ExperienceEntity updateExperience(String id, ExperienceEntity updatedExperience);
    
    List<ExperienceEntity> getExperiencesByTag(String tag);
    
    List<ExperienceEntity> getExperiencesByMultipleTags(List<String> tags);

}
