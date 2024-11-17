package com.igrowker.wander.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDto {

    private String id;
    private String name;
    private String email;
    private String role;
    private List<String> preferences;
    private String location;
    private LocalDateTime createdAt;
}
