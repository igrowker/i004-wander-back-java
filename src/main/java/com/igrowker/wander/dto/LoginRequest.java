
package com.igrowker.wander.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;


public class LoginRequest {
    
    @Email(message = "Invalid email format")
    @NotNull(message = "Email is required")
    private String email;
    
    @NotNull(message = "Password is required")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}