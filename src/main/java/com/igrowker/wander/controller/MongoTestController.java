package com.igrowker.wander.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
//Endpoint for verify the connection to MongoDB
public class MongoTestController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/check-mongo")
    public ResponseEntity<String> checkMongoConnection() {
        try {
        	// Run a query to check if the database connection is working.
            mongoTemplate.getDb().listCollectionNames();

         // If it runs without exceptions, the connection is successful.
            return ResponseEntity.ok("La conexión con MongoDB fue exitosa!");
        } catch (Exception e) {
        	// If an exception occurs, respond with the error and a server error status.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al conectar con MongoDB: " + e.getMessage());
        }
    }
}
