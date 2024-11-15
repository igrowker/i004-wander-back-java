package com.igrowker.wander.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExpiredJwtException extends RuntimeException {
    private String message;
}