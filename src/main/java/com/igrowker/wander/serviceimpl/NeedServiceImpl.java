package com.igrowker.wander.serviceimpl;

import com.igrowker.wander.entity.Need;
import com.igrowker.wander.repository.NeedRepository;
import com.igrowker.wander.service.NeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NeedServiceImpl implements NeedService {

    @Autowired
    private NeedRepository needRepository;

    @Override
    public List<Need> getNeedsByExperienceId(String experienceId) {
        return needRepository.findByExperienceId(experienceId);
    }
}
