package com.igrowker.wander.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
	
	@NotBlank
	private String experienceId;
	
	@NotBlank
	private String userId;
	
	@NotBlank
	@Min(1)
    @Max(5)
	private Double rating; 
	
	private String comment;
	
	@NotBlank
	private Date createdAt = new Date();
}
