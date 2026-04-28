package com.matryoshkaja.demo.Security;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SecurityConfigTest {

    private final SecurityConfig config =
            new SecurityConfig(mock(JwtAuthenticationFilter.class));

    @Test
    void shouldCreatePasswordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();
        assertNotNull(encoder);
    }

    @Test
    void shouldCreateAuthenticationManager() throws Exception {
        AuthenticationConfiguration configuration = mock(AuthenticationConfiguration.class);
        AuthenticationManager manager = mock(AuthenticationManager.class);

        when(configuration.getAuthenticationManager()).thenReturn(manager);

        AuthenticationManager result = config.authenticationManager(configuration);

        assertEquals(manager, result);
    }
}