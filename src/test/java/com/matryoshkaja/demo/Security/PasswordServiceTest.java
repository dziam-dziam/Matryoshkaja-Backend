package com.matryoshkaja.demo.Security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordServiceTest {

    private PasswordService passwordService;

    @BeforeEach
    void setUp(){
        passwordService = new PasswordService();
    }

    @Test
    void shouldHashPassword(){
        //given
        String rawPassword = "Password123!";
        //when
        String hashedPassword = passwordService.hash(rawPassword);
        //then
        assertNotEquals(rawPassword,hashedPassword);
    }

    @Test
    void shouldMatchCorrectPasswordWithItsHash(){
        //given
        String rawPassword = "Password123!";
        String hashedPassword = passwordService.hash(rawPassword);
        //when
        boolean result = passwordService.matches(rawPassword,hashedPassword);
        //then
        assertTrue(result);
    }
    @Test
    void shouldNotMatchIncorrectPasswordWithOtherHash() {
        //given
        String rawPassword = "Password123!";
        String incorrectPassword = "Password456?";

        String hashedPassword = passwordService.hash(rawPassword);
        //when
        boolean result = passwordService.matches(incorrectPassword, hashedPassword);
        //then
        assertFalse(result);
    }

}