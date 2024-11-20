package com.igrowker.wander.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reviews") 
public class ReviewEntity {

	@Id
    private String id; 
	
	@NotNull
	private String experienceId;
	
	@NotNull
	private String userId;
	
	@NotNull
	@Min(1)
    @Max(5)
	private Double rating; 
	
	private String comment;
	
	@NotNull
	private Date createdAt = new Date();
}
