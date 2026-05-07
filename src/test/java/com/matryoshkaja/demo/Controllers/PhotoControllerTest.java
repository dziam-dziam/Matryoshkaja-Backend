package com.matryoshkaja.demo.Controllers;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoCaptionUpdateDto;
import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Exceptions.PhotoNotFoundException;
import com.matryoshkaja.demo.Security.CustomUserDetailsService;
import com.matryoshkaja.demo.Security.JwtService;
import com.matryoshkaja.demo.Services.PhotoServices.DeletePhotoService;
import com.matryoshkaja.demo.Services.PhotoServices.GetPhotoService;
import com.matryoshkaja.demo.Services.PhotoServices.UpdatePhotoCaptionService;
import com.matryoshkaja.demo.Services.PhotoServices.UpdatePhotoOrderService;
import com.matryoshkaja.demo.Services.PhotoServices.UploadPhotoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhotoController.class)
class PhotoControllerTest {

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private MockMvc mockMvc;

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
    @WithMockUser
    void shouldUploadPhoto() throws Exception {
        // given
        MockMultipartFile file =
                new MockMultipartFile("file", "test.png", "image/png", "data".getBytes());

        PhotoResponseDto dto = PhotoResponseDto.builder()
                .id(1L)
                .imageUrl("url")
                .caption("Fresh caption")
                .displayOrder(1)
                .build();

        when(uploadPhotoService.uploadPhoto(any(MultipartFile.class), eq("Fresh caption")))
                .thenReturn(dto);

        // when / then
        mockMvc.perform(multipart("/photos")
                        .file(file)
                        .param("caption", "Fresh caption")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.imageUrl").value("url"))
                .andExpect(jsonPath("$.caption").value("Fresh caption"))
                .andExpect(jsonPath("$.displayOrder").value(1));

        verify(uploadPhotoService).uploadPhoto(any(MultipartFile.class), eq("Fresh caption"));
    }

    @Test
    @WithMockUser
    void shouldUploadPhotoWithoutCaption() throws Exception {
        // given
        MockMultipartFile file =
                new MockMultipartFile("file", "test.png", "image/png", "data".getBytes());

        PhotoResponseDto dto = PhotoResponseDto.builder()
                .id(1L)
                .imageUrl("url")
                .caption("")
                .displayOrder(1)
                .build();

        when(uploadPhotoService.uploadPhoto(any(MultipartFile.class), nullable(String.class)))
                .thenReturn(dto);

        // when / then
        mockMvc.perform(multipart("/photos")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.imageUrl").value("url"))
                .andExpect(jsonPath("$.caption").value(""))
                .andExpect(jsonPath("$.displayOrder").value(1));

        verify(uploadPhotoService).uploadPhoto(any(MultipartFile.class), nullable(String.class));
    }

    @Test
    @WithMockUser
    void shouldGetPhoto() throws Exception {
        // given
        PhotoResponseDto dto = PhotoResponseDto.builder()
                .id(1L)
                .imageUrl("url")
                .caption("Caption")
                .displayOrder(2)
                .build();

        when(getPhotoService.getPhoto(1L)).thenReturn(dto);

        // when / then
        mockMvc.perform(get("/photos/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.imageUrl").value("url"))
                .andExpect(jsonPath("$.caption").value("Caption"))
                .andExpect(jsonPath("$.displayOrder").value(2));
    }

    @Test
    @WithMockUser
    void shouldGetAllPhotos() throws Exception {
        // given
        List<PhotoResponseDto> responseDtos = List.of(
                PhotoResponseDto.builder()
                        .id(1L)
                        .imageUrl("url1")
                        .caption("Caption 1")
                        .displayOrder(1)
                        .build(),
                PhotoResponseDto.builder()
                        .id(2L)
                        .imageUrl("url2")
                        .caption("Caption 2")
                        .displayOrder(2)
                        .build()
        );

        when(getPhotoService.getAllPhotos()).thenReturn(responseDtos);

        // when / then
        mockMvc.perform(get("/photos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].imageUrl").value("url1"))
                .andExpect(jsonPath("$[0].caption").value("Caption 1"))
                .andExpect(jsonPath("$[0].displayOrder").value(1))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].imageUrl").value("url2"))
                .andExpect(jsonPath("$[1].caption").value("Caption 2"))
                .andExpect(jsonPath("$[1].displayOrder").value(2));
    }

    @Test
    @WithMockUser
    void shouldDeletePhoto() throws Exception {
        // given
        Long photoId = 1L;

        // when / then
        mockMvc.perform(delete("/photos/{id}", photoId)
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(deletePhotoService).deletePhoto(photoId);
    }

    @Test
    @WithMockUser
    void shouldUpdateCaption() throws Exception {
        // given
        PhotoResponseDto dto = PhotoResponseDto.builder()
                .id(1L)
                .imageUrl("url")
                .caption("Updated caption")
                .displayOrder(1)
                .build();

        when(updatePhotoCaptionService.updateCaption(eq(1L), any(PhotoCaptionUpdateDto.class)))
                .thenReturn(dto);

        // when / then
        mockMvc.perform(patch("/photos/1/caption")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"caption\":\"Updated caption\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.imageUrl").value("url"))
                .andExpect(jsonPath("$.caption").value("Updated caption"))
                .andExpect(jsonPath("$.displayOrder").value(1));

        verify(updatePhotoCaptionService).updateCaption(
                eq(1L),
                argThat(request -> "Updated caption".equals(request.getCaption()))
        );
    }

    @Test
    @WithMockUser
    void shouldUpdatePhotoOrder() throws Exception {
        // given
        List<PhotoResponseDto> responseDtos = List.of(
                PhotoResponseDto.builder()
                        .id(2L)
                        .imageUrl("url2")
                        .caption("Caption 2")
                        .displayOrder(1)
                        .build(),
                PhotoResponseDto.builder()
                        .id(1L)
                        .imageUrl("url1")
                        .caption("Caption 1")
                        .displayOrder(2)
                        .build()
        );

        when(updatePhotoOrderService.updateOrder(any())).thenReturn(responseDtos);

        // when / then
        mockMvc.perform(put("/photos/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"photoIds\":[2,1]}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].displayOrder").value(1))
                .andExpect(jsonPath("$[1].id").value(1L))
                .andExpect(jsonPath("$[1].displayOrder").value(2));

        verify(updatePhotoOrderService).updateOrder(argThat(request ->
                request.getPhotoIds().equals(List.of(2L, 1L))
        ));
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenPhotoNotFound() throws Exception {
        // given
        when(getPhotoService.getPhoto(1L))
                .thenThrow(new PhotoNotFoundException(1L));

        // when / then
        mockMvc.perform(get("/photos/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/photos/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenPathIsWrong() throws Exception {
        mockMvc.perform(get("/photos/"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenFileIsEmpty() throws Exception {
        MockMultipartFile file =
                new MockMultipartFile("file", "", "image/png", new byte[0]);

        when(uploadPhotoService.uploadPhoto(any(MultipartFile.class), nullable(String.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(multipart("/photos")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn401WhenUserNotAuthenticated() throws Exception {
        mockMvc.perform(get("/photos/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldReturn403WhenDeletingWithoutCsrf() throws Exception {
        mockMvc.perform(delete("/photos/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenFileIsMissing() throws Exception {
        mockMvc.perform(multipart("/photos")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturn404WhenDeletingNonExistingPhoto() throws Exception {
        doThrow(new PhotoNotFoundException(1L))
                .when(deletePhotoService).deletePhoto(1L);

        mockMvc.perform(delete("/photos/1").with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturnEmptyList() throws Exception {
        when(getPhotoService.getAllPhotos()).thenReturn(List.of());

        mockMvc.perform(get("/photos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser
    void shouldReturn400WhenFileIsWrongType() throws Exception {
        MockMultipartFile file =
                new MockMultipartFile("file", "test.txt", "text/plain", "data".getBytes());

        when(uploadPhotoService.uploadPhoto(any(MultipartFile.class), nullable(String.class)))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(multipart("/photos")
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}
