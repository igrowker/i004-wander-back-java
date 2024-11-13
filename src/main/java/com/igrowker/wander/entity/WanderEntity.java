package com.igrowker.wander.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//Provisional class to generate the MVC structure
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WanderEntity {
	 @Id
	    private Long id;
}
