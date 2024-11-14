package com.igrowker.wander.entity;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull; 

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

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String location;

    @NotNull
    @Field("hostId") 
    private String hostId; 

    @NotNull
    private double price;

    private List<String> availabilityDates;

    private List<String> tags;

    private double rating;

    @NotNull
    private int capacity;

    private Date createdAt = new Date();
}
