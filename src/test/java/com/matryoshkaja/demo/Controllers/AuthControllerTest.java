package com.matryoshkaja.demo.Controllers;

import com.matryoshkaja.demo.Security.CustomUserDetailsService;
import com.matryoshkaja.demo.Security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldLoginAndReturnToken() throws Exception {
        String requestJson = """
        {
          "email": "test@mail.com",
          "password": "Test123!"
        }
        """;

        when(jwtService.generateToken("test@mail.com"))
                .thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("mocked-jwt-token"));

        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken("test@mail.com");
    }

    // ---------- BAD CREDENTIALS ----------

    @Test
    void shouldReturn403WhenBadCredentials() throws Exception {
        String requestJson = """
        {
          "email": "test@mail.com",
          "password": "wrong"
        }
        """;

        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any());

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verify(authenticationManager).authenticate(any());
        verify(jwtService, never()).generateToken(any());
    }

}