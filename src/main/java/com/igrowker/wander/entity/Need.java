package com.igrowker.wander.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "needs")
public class Need {

    @Id
    private String id;
    private String experienceId; 
    private String type;         
    private int quantity;        
    private String status;
}
