package com.igrowker.wander.dto.experience;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseExperienceDto {
    private String id;
    private String title;
    private String description;
    private List<String> location;
    private String hostId;
    private Double price;
    private List<String> availabilityDates;
    private List<String> experienceImages;
    private List<String> tags;
    private Double rating;
    private int capacity;
    private Date createdAt;
    private boolean status;
}
