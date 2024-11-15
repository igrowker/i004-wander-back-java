package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.entity.ExperienceEntity;
import com.igrowker.wander.repository.ExperienceRepository;
import com.igrowker.wander.service.ExperienceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExperienceServiceImpl implements ExperienceService {

    @Autowired
    private ExperienceRepository experienceRepository;

    @Override
    public ExperienceEntity createExperience(ExperienceEntity experience) {
    
        return experienceRepository.save(experience);
    }
}
