package com.igrowker.wander.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private Long id;
    private String name;
    private String email;
    private String passwordHash;
    private String role;
    private List<String> preferences;
    private String location;
    private LocalDateTime createdAt;
    private List<String> bookings;
}
