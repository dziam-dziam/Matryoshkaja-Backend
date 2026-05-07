package com.matryoshkaja.demo.Security;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    private final CorsConfigurationSource source =
            new CorsConfig().corsConfigurationSource();

    @Test
    void shouldRegisterCorsConfigurationForEveryPath() {
        CorsConfiguration configuration = getConfigurationFor("/photos");

        assertNotNull(configuration);
        assertEquals(List.of(
                "https://matryoshkaja-frontend.vercel.app",
                "https://*.vercel.app",
                "http://localhost:4200"
        ), configuration.getAllowedOriginPatterns());
        assertEquals(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"),
                configuration.getAllowedMethods());
        assertEquals(List.of("Authorization", "Content-Type"),
                configuration.getAllowedHeaders());
        assertTrue(configuration.getAllowCredentials());
    }

    @Test
    void shouldAllowProductionFrontendOrigin() {
        CorsConfiguration configuration = getConfigurationFor("/photos");

        assertEquals("https://matryoshkaja-frontend.vercel.app",
                configuration.checkOrigin("https://matryoshkaja-frontend.vercel.app"));
    }

    @Test
    void shouldAllowVercelPreviewOrigins() {
        CorsConfiguration configuration = getConfigurationFor("/photos");

        assertEquals("https://matryoshkaja-frontend-git-main-user.vercel.app",
                configuration.checkOrigin("https://matryoshkaja-frontend-git-main-user.vercel.app"));
        assertEquals("https://matryoshkaja-frontend-abc123.vercel.app",
                configuration.checkOrigin("https://matryoshkaja-frontend-abc123.vercel.app"));
    }

    @Test
    void shouldAllowLocalAngularDevServer() {
        CorsConfiguration configuration = getConfigurationFor("/photos");

        assertEquals("http://localhost:4200",
                configuration.checkOrigin("http://localhost:4200"));
    }

    @Test
    void shouldRejectOriginsOutsideWhitelist() {
        CorsConfiguration configuration = getConfigurationFor("/photos");

        assertNull(configuration.checkOrigin("https://evil.example"));
        assertNull(configuration.checkOrigin("http://localhost:3000"));
        assertNull(configuration.checkOrigin("https://matryoshkaja.onrender.com"));
    }

    @Test
    void shouldAllowConfiguredHttpMethods() {
        CorsConfiguration configuration = getConfigurationFor("/photos");

        assertIterableEquals(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                        HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.OPTIONS),
                configuration.checkHttpMethod(HttpMethod.GET));
        assertIterableEquals(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                        HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.OPTIONS),
                configuration.checkHttpMethod(HttpMethod.POST));
        assertIterableEquals(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                        HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.OPTIONS),
                configuration.checkHttpMethod(HttpMethod.PUT));
        assertIterableEquals(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                        HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.OPTIONS),
                configuration.checkHttpMethod(HttpMethod.PATCH));
        assertIterableEquals(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                        HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.OPTIONS),
                configuration.checkHttpMethod(HttpMethod.DELETE));
        assertIterableEquals(List.of(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT,
                        HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.OPTIONS),
                configuration.checkHttpMethod(HttpMethod.OPTIONS));
    }

    @Test
    void shouldRejectMethodsOutsideWhitelist() {
        CorsConfiguration configuration = getConfigurationFor("/photos");

        assertNull(configuration.checkHttpMethod(HttpMethod.TRACE));
    }

    @Test
    void shouldAllowConfiguredHeadersOnly() {
        CorsConfiguration configuration = getConfigurationFor("/photos");

        assertEquals(List.of("Authorization"), configuration.checkHeaders(List.of("Authorization")));
        assertEquals(List.of("Content-Type"), configuration.checkHeaders(List.of("Content-Type")));
        assertEquals(List.of("Authorization", "Content-Type"),
                configuration.checkHeaders(List.of("Authorization", "Content-Type")));
        assertNull(configuration.checkHeaders(List.of("X-Requested-With")));
    }

    private CorsConfiguration getConfigurationFor(String path) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", path);
        return source.getCorsConfiguration(request);
    }
}