package com.matryoshkaja.demo.Security;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private JwtService jwtService;

    private final String SECRET = "mysecretkeymysecretkeymysecretkey12";
    private final long EXPIRATION = 3600000;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", EXPIRATION);
    }

    @Test
    @DisplayName("should generate valid token")
    void shouldGenerateValidToken() {
        // given
        String email = "test@gmail.com";

        // when
        String token = jwtService.generateToken(email);

        // then
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("should extract username from token")
    void shouldExtractUsernameFromToken() {
        // given
        String email = "test@gmail.com";
        String token = jwtService.generateToken(email);

        // when
        String extracted = jwtService.extractUsername(token);

        // then
        assertEquals(email, extracted);
    }

    @Test
    @DisplayName("should validate correct token")
    void shouldValidateCorrectToken() {
        // given
        String email = "test@gmail.com";
        String token = jwtService.generateToken(email);

        // when
        boolean isValid = jwtService.isTokenValid(token, email);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("should invalidate token for wrong user")
    void shouldInvalidateTokenForWrongUser() {
        // given
        String token = jwtService.generateToken("test@gmail.com");

        // when
        boolean isValid = jwtService.isTokenValid(token, "other@gmail.com");

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("should detect expired token")
    void shouldThrowWhenTokenExpired() throws InterruptedException {
        // given → bardzo krótka ważność
        ReflectionTestUtils.setField(jwtService, "expiration", 1);

        String token = jwtService.generateToken("test@gmail.com");

        Thread.sleep(5);

        // when & then
        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, "test@gmail.com"));
    }

}