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
public class MongoTestController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/check-mongo")
    public ResponseEntity<String> checkMongoConnection() {
        try {
            // Ejecuta una consulta para verificar si la conexión a la base de datos funciona.
            mongoTemplate.getDb().listCollectionNames();

            // Si se ejecuta sin excepciones, la conexión es exitosa.
            return ResponseEntity.ok("La conexión con MongoDB fue exitosa!");
        } catch (Exception e) {
            // Si ocurre una excepción, responde con el error y un estado de error del servidor.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al conectar con MongoDB: " + e.getMessage());
        }
    }
}
