package com.igrowker.wander.exception;

public record ErrorResponse(
        int statusCode,
        String message
) {
}