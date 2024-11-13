package com.igrowker.wander.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
//Provisional class to generate the MVC structure
@Entity
public class WanderEntity {
	 @Id
	    private Long id;
	 
	 // Getters & Setters
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }
}
