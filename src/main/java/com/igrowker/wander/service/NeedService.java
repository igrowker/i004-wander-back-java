package com.igrowker.wander.service;

import com.igrowker.wander.entity.Need;
import java.util.List;

public interface NeedService {
    List<Need> getNeedsByExperienceId(String experienceId);
}
