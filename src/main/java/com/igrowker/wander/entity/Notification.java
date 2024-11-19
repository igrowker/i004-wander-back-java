package com.igrowker.wander.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    @NotNull
    private String userId;

    @NotNull
    private String type;

    @NotNull
    private String message;

    @NotNull
    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();


}
