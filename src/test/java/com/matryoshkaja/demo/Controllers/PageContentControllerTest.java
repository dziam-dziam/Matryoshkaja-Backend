package com.matryoshkaja.demo.Controllers;

import com.matryoshkaja.demo.Dtos.PageContentDto;
import com.matryoshkaja.demo.Security.CustomUserDetailsService;
import com.matryoshkaja.demo.Security.JwtService;
import com.matryoshkaja.demo.Services.PageContentServices.GetPageContentService;
import com.matryoshkaja.demo.Services.PageContentServices.UpdatePageContentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PageContentController.class)
class PageContentControllerTest {

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private GetPageContentService getPageContentService;

    @MockitoBean
    private UpdatePageContentService updatePageContentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void shouldGetAllPageContent() throws Exception {
        // given
        List<PageContentDto> response = List.of(
                PageContentDto.builder()
                        .key("about.title")
                        .value("Title")
                        .build(),
                PageContentDto.builder()
                        .key("about.description")
                        .value("Description")
                        .build()
        );

        when(getPageContentService.getAll()).thenReturn(response);

        // when / then
        mockMvc.perform(get("/page-content"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].key").value("about.title"))
                .andExpect(jsonPath("$[0].value").value("Title"))
                .andExpect(jsonPath("$[1].key").value("about.description"))
                .andExpect(jsonPath("$[1].value").value("Description"));

        verify(getPageContentService).getAll();
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyListWhenNoPageContentExists() throws Exception {
        // given
        when(getPageContentService.getAll()).thenReturn(List.of());

        // when / then
        mockMvc.perform(get("/page-content"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isEmpty());

        verify(getPageContentService).getAll();
    }

    @Test
    @WithMockUser
    void shouldUpdateAllPageContent() throws Exception {
        // given
        List<PageContentDto> response = List.of(
                PageContentDto.builder()
                        .key("about.title")
                        .value("Updated title")
                        .build(),
                PageContentDto.builder()
                        .key("about.description")
                        .value("Updated description")
                        .build()
        );

        when(updatePageContentService.updateAll(argThat(request ->
                request.size() == 2 &&
                        "about.title".equals(request.get(0).getKey()) &&
                        "Updated title".equals(request.get(0).getValue()) &&
                        "about.description".equals(request.get(1).getKey()) &&
                        "Updated description".equals(request.get(1).getValue())
        ))).thenReturn(response);

        // when / then
        mockMvc.perform(put("/page-content")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                [
                                  {
                                    "key": "about.title",
                                    "value": "Updated title"
                                  },
                                  {
                                    "key": "about.description",
                                    "value": "Updated description"
                                  }
                                ]
                                """))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].key").value("about.title"))
                .andExpect(jsonPath("$[0].value").value("Updated title"))
                .andExpect(jsonPath("$[1].key").value("about.description"))
                .andExpect(jsonPath("$[1].value").value("Updated description"));

        verify(updatePageContentService).updateAll(argThat(request ->
                request.size() == 2 &&
                        "about.title".equals(request.get(0).getKey()) &&
                        "Updated title".equals(request.get(0).getValue()) &&
                        "about.description".equals(request.get(1).getKey()) &&
                        "Updated description".equals(request.get(1).getValue())
        ));
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenUpdateRequestIsInvalid() throws Exception {
        // given
        when(updatePageContentService.updateAll(argThat(request ->
                request.size() == 1 &&
                        request.getFirst().getKey() == null &&
                        "Value".equals(request.getFirst().getValue())
        ))).thenThrow(new IllegalArgumentException("Content key cannot be empty"));

        // when / then
        mockMvc.perform(put("/page-content")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                [
                                  {
                                    "key": null,
                                    "value": "Value"
                                  }
                                ]
                                """))
                .andExpect(status().isBadRequest());

        verify(updatePageContentService).updateAll(argThat(request ->
                request.size() == 1 &&
                        request.getFirst().getKey() == null &&
                        "Value".equals(request.getFirst().getValue())
        ));
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenRequestBodyIsMissing() throws Exception {
        mockMvc.perform(put("/page-content")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenUpdatingWithoutCsrf() throws Exception {
        mockMvc.perform(put("/page-content")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                [
                                  {
                                    "key": "about.title",
                                    "value": "Title"
                                  }
                                ]
                                """))
                .andExpect(status().isForbidden());
    }
}
