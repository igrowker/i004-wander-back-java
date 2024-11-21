package com.igrowker.wander.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "experiences") 
public class ExperienceEntity {

    @Id
    private String id; 

    private String title;

    private String description;

    private String location;

    @Field("hostId") 
    private String hostId; 

    private Double price;

    private List<String> availabilityDates;

    private List<String> tags;

    private Double rating;

    private int capacity;

    private Date createdAt = new Date();
    
    private boolean status = true;
}
