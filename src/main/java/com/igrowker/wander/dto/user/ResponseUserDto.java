package com.igrowker.wander.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUserDto {

    private String id;
    private String name;
    private String phone;
    private String email;
    private String role;
    private List<String> preferences;
    private String location;
    private Date createdAt;
    private String verificationCode;

}
