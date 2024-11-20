package com.igrowker.wander.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestVerifyUserDto {
    private String email;
    private String verificationCode;
}
