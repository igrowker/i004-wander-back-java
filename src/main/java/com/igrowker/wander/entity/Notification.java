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
    private Boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    public void setUserId(String userId) {
        if (userId == null) {
            throw new NullPointerException("userId is marked non-null but is null");
        }
        this.userId = userId;
    }

    public void setType(String type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        this.type = type;
    }

    public void setMessage(String message) {
        if (message == null) {
            throw new NullPointerException("message is marked non-null but is null");
        }
        this.message = message;
    }
}
