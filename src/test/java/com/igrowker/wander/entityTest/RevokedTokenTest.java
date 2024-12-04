package com.igrowker.wander.entityTest;

import com.igrowker.wander.entity.RevokedToken;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class RevokedTokenTest {
    @Test
    void shouldCreateRevokedTokenSuccessfully() {
        // Given
        String expectedToken = "sample-token";
        Date expectedExpirationDate = new Date();

        // When
        RevokedToken revokedToken = new RevokedToken(expectedToken, expectedExpirationDate);

        // Then
        assertEquals(expectedToken, revokedToken.getToken(), "Token should match the given value");
        assertEquals(expectedExpirationDate, revokedToken.getExpirationDate(), "Expiration date should match the given value");
    }

    @Test
    void shouldSetAndGetToken() {
        // Given
        RevokedToken revokedToken = new RevokedToken(null, null);
        String newToken = "new-token";

        // When
        revokedToken.setToken(newToken);

        // Then
        assertEquals(newToken, revokedToken.getToken(), "Token should be updated to the new value");
    }

    @Test
    void shouldSetAndGetExpirationDate() {
        // Given
        RevokedToken revokedToken = new RevokedToken(null, null);
        Date newExpirationDate = new Date();

        // When
        revokedToken.setExpirationDate(newExpirationDate);

        // Then
        assertEquals(newExpirationDate, revokedToken.getExpirationDate(), "Expiration date should be updated to the new value");
    }
}
