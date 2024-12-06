package com.igrowker.wander.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reviews")
@CompoundIndex(name = "experience_createdAt_idx", def = "{'experienceId': 1, 'createdAt': -1}")
public class ReviewEntity {

	@Id
    private String id; 
	
	private String experienceId;
	
	private String userId;

	private String userAvatar;

	private String userName;
	
	private Double rating; 
	
	private String comment;
	
	private Date createdAt = new Date();


}
