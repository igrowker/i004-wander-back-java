package com.igrowker.wander.controller;

import com.igrowker.wander.entity.Need;
import com.igrowker.wander.service.NeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/needs")
public class NeedController {

    @Autowired
    private NeedService needService;

    @GetMapping("/{experienceId}")
    public ResponseEntity<List<Need>> getNeedsByExperienceId(@PathVariable String experienceId) {
        List<Need> needs = needService.getNeedsByExperienceId(experienceId);
        if (needs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(needs);
    }
}
