
package com.igrowker.wander.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 * @author AdolfoJF
 */
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String username;
    private String password;

    // Constructores
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}