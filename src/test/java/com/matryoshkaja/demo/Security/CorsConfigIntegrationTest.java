package com.matryoshkaja.demo.Security;

import com.matryoshkaja.demo.Controllers.PhotoController;
import com.matryoshkaja.demo.Services.PhotoServices.DeletePhotoService;
import com.matryoshkaja.demo.Services.PhotoServices.GetPhotoService;
import com.matryoshkaja.demo.Services.PhotoServices.UpdatePhotoCaptionService;
import com.matryoshkaja.demo.Services.PhotoServices.UpdatePhotoOrderService;
import com.matryoshkaja.demo.Services.PhotoServices.UploadPhotoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhotoController.class)
@Import({CorsConfig.class, SecurityConfig.class, JwtAuthenticationFilter.class})
class CorsConfigIntegrationTest {

    private static final String PRODUCTION_ORIGIN = "https://matryoshkaja-frontend.vercel.app";
    private static final String PREVIEW_ORIGIN = "https://matryoshkaja-frontend-git-main-user.vercel.app";
    private static final String LOCAL_ORIGIN = "http://localhost:4200";
    private static final String BLOCKED_ORIGIN = "https://evil.example";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private UploadPhotoService uploadPhotoService;

    @MockitoBean
    private DeletePhotoService deletePhotoService;

    @MockitoBean
    private GetPhotoService getPhotoService;

    @MockitoBean
    private UpdatePhotoOrderService updatePhotoOrderService;

    @MockitoBean
    private UpdatePhotoCaptionService updatePhotoCaptionService;

    @Test
    void shouldReturnCorsHeadersForProductionGetRequest() throws Exception {
        when(getPhotoService.getAllPhotos()).thenReturn(List.of());

        mockMvc.perform(get("/photos")
                        .header(HttpHeaders.ORIGIN, PRODUCTION_ORIGIN))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, PRODUCTION_ORIGIN))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));
    }

    @Test
    void shouldAllowProductionPreflightRequest() throws Exception {
        mockMvc.perform(options("/photos")
                        .header(HttpHeaders.ORIGIN, PRODUCTION_ORIGIN)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Authorization, Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, PRODUCTION_ORIGIN))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                        "GET,POST,PUT,PATCH,DELETE,OPTIONS"))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                        "Authorization, Content-Type"));
    }

    @Test
    void shouldAllowVercelPreviewPreflightRequest() throws Exception {
        mockMvc.perform(options("/photos")
                        .header(HttpHeaders.ORIGIN, PREVIEW_ORIGIN)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, PREVIEW_ORIGIN))
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true"));
    }

    @Test
    void shouldAllowLocalhostPreflightRequest() throws Exception {
        mockMvc.perform(options("/photos")
                        .header(HttpHeaders.ORIGIN, LOCAL_ORIGIN)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, LOCAL_ORIGIN));
    }


    @Test
    void shouldRejectPreflightFromBlockedOrigin() throws Exception {
        mockMvc.perform(options("/photos")
                        .header(HttpHeaders.ORIGIN, BLOCKED_ORIGIN)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    void shouldRejectPreflightWithBlockedMethod() throws Exception {
        mockMvc.perform(options("/photos")
                        .header(HttpHeaders.ORIGIN, PRODUCTION_ORIGIN)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "TRACE"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    void shouldRejectPreflightWithBlockedHeader() throws Exception {
        mockMvc.perform(options("/photos")
                        .header(HttpHeaders.ORIGIN, PRODUCTION_ORIGIN)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "X-Requested-With"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }

    @Test
    void shouldNotAddCorsHeadersWhenOriginHeaderIsMissing() throws Exception {
        when(getPhotoService.getAllPhotos()).thenReturn(List.of());

        mockMvc.perform(get("/photos"))
                .andExpect(status().isOk())
                .andExpect(header().doesNotExist(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN));
    }
}