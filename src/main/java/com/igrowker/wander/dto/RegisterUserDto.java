package com.igrowker.wander.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    private String name;
    private String email;
    private String password;
    private String role;
    private List<String> preferences;
    private String location;
}
