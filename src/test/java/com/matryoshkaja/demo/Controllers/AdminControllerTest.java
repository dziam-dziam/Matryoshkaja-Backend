package com.matryoshkaja.demo.Controllers;


import com.matryoshkaja.demo.Dtos.AdminDtos.AdminResponseDto;
import com.matryoshkaja.demo.Exceptions.AdminNotFoundException;
import com.matryoshkaja.demo.Services.AdminServices.CreateAdminService;
import com.matryoshkaja.demo.Services.AdminServices.GetAdminServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CreateAdminService createAdminService;
    @MockitoBean
    private GetAdminServices getAdminServices;


    @Test
    @WithMockUser
    void shouldCreateAdmin() throws Exception {
        String requestJson = """
            {
              "email": "test@mail.com",
              "password": "Test123!"
            }
            """;

        AdminResponseDto response = AdminResponseDto.builder()
                .id(1L)
                .email("test@mail.com")
                .build();

        when(createAdminService.createAdmin(any())).thenReturn(response);

        mockMvc.perform(post("/admins")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.email").exists());

        verify(createAdminService).createAdmin(any());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenCreateInvalid() throws Exception {
        String requestJson = "{}"; // brak danych

        mockMvc.perform(post("/admins")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn403WhenCreateWithoutAuth() throws Exception {
        mockMvc.perform(post("/admins")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    // ---------- GET ONE ----------

    @Test
    @WithMockUser
    void shouldGetAdmin() throws Exception {
        AdminResponseDto dto = new AdminResponseDto(1L, "test@mail.com");

        when(getAdminServices.getAdmin(1L)).thenReturn(dto);

        mockMvc.perform(get("/admins/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@mail.com"))
                .andExpect(jsonPath("$.email").exists());
        verify(getAdminServices).getAdmin(1L);
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenAdminNotFound() throws Exception {
        when(getAdminServices.getAdmin(1L))
                .thenThrow(new AdminNotFoundException(1L));

        mockMvc.perform(get("/admins/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenIdInvalid() throws Exception {
        mockMvc.perform(get("/admins/abc"))
                .andExpect(status().isBadRequest());
    }

    // ---------- GET ALL ----------

    @Test
    @WithMockUser
    void shouldGetAllAdmins() throws Exception {
        List<AdminResponseDto> list = List.of(
                new AdminResponseDto(1L, "a@mail.com"),
                new AdminResponseDto(2L, "b@mail.com")
        );

        when(getAdminServices.getAllAdmins()).thenReturn(list);

        mockMvc.perform(get("/admins"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[0].email").exists())
                .andExpect(jsonPath("$[1].email").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyList() throws Exception {
        when(getAdminServices.getAllAdmins()).thenReturn(List.of());

        mockMvc.perform(get("/admins"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$").isEmpty());
    }

    // ---------- SECURITY ----------

    @Test
    void shouldReturn401WhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/admins"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenCreateWithoutCsrf() throws Exception {
        mockMvc.perform(post("/admins")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenPasswordInvalid() throws Exception {
        String requestJson = """
        {
          "email": "test@mail.com",
          "password": "123"
        }
        """;

        mockMvc.perform(post("/admins")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenEmailInvalid() throws Exception {
        String requestJson = """
        {
          "email": "wrong-email",
          "password": "Test123!"
        }
        """;

        mockMvc.perform(post("/admins")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenBodyMissing() throws Exception {
        mockMvc.perform(post("/admins")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn415WhenWrongContentType() throws Exception {
        mockMvc.perform(post("/admins")
                        .content("some text")
                        .with(csrf()))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenWrongPath() throws Exception {
        mockMvc.perform(get("/adminss"))
                .andExpect(status().isNotFound());
    }
}