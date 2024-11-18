package com.igrowker.wander.dto;

import com.igrowker.wander.entity.ERole;

public class RegisterRequest {
    String username;
    String email;
    String password;
    ERole role;

    public RegisterRequest(String username, ERole role, String password, String email) {
        this.username = username;
        this.role = role;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public ERole getRole() {
        return role;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

}
