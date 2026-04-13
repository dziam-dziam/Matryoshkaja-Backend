package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Dtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Exceptions.PhotoNotFoundException;
import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetPhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private PhotoMapper photoMapper;

    @InjectMocks
    private GetPhotoService getPhotoService;

    @Test
    void shouldGetPhoto() {
        // given
        Long photoId = 1L;

        Photo photo = Photo.builder()
                .id(photoId)
                .imageUrl("url")
                .imageKey("key")
                .build();

        PhotoResponseDto responseDto = PhotoResponseDto.builder()
                .id(photoId)
                .imageUrl("url")
                .build();

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(photoMapper.mapEntityToResponseDto(photo)).thenReturn(responseDto);

        // when
        PhotoResponseDto result = getPhotoService.getPhoto(photoId);

        // then
        assertNotNull(result);
        assertEquals(photoId, result.getId());
        assertEquals("url", result.getImageUrl());

        verify(photoRepository).findById(photoId);
        verify(photoMapper).mapEntityToResponseDto(photo);
    }

    @Test
    void shouldThrowWhenIdIsNull() {
        // given null / when / then
        assertThrows(IllegalArgumentException.class,
                () -> getPhotoService.getPhoto(null));

        verify(photoRepository, never()).findById(any(Long.class));
        verify(photoMapper, never()).mapEntityToResponseDto(any(Photo.class));
    }

    @Test
    void shouldThrowWhenPhotoIsNotFound() {
        // given
        Long photoId = 1L;
        when(photoRepository.findById(photoId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(PhotoNotFoundException.class,
                () -> getPhotoService.getPhoto(photoId));

        verify(photoRepository).findById(photoId);
        verify(photoMapper, never()).mapEntityToResponseDto(any(Photo.class));
    }

    @Test
    void shouldGetAllPhotos() {
        // given
        List<Photo> photos = List.of(
                Photo.builder().id(1L).imageUrl("url1").imageKey("key1").build(),
                Photo.builder().id(2L).imageUrl("url2").imageKey("key2").build()
        );

        List<PhotoResponseDto> responseDtos = List.of(
                PhotoResponseDto.builder().id(1L).imageUrl("url1").build(),
                PhotoResponseDto.builder().id(2L).imageUrl("url2").build()
        );

        when(photoRepository.findAll()).thenReturn(photos);
        when(photoMapper.mapEntityToResponseDto(photos.get(0))).thenReturn(responseDtos.get(0));
        when(photoMapper.mapEntityToResponseDto(photos.get(1))).thenReturn(responseDtos.get(1));

        // when
        List<PhotoResponseDto> result = getPhotoService.getAllPhotos();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.getFirst().getId());
        assertEquals("url1", result.getFirst().getImageUrl());
        assertEquals(2L, result.get(1).getId());
        assertEquals("url2", result.get(1).getImageUrl());

        verify(photoRepository).findAll();
        verify(photoMapper).mapEntityToResponseDto(photos.get(0));
        verify(photoMapper).mapEntityToResponseDto(photos.get(1));
    }

    @Test
    void shouldReturnEmptyWhenNoPhotosExist() {
        // given
        when(photoRepository.findAll()).thenReturn(List.of());

        // when
        List<PhotoResponseDto> result = getPhotoService.getAllPhotos();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(photoRepository).findAll();
        verify(photoMapper, never()).mapEntityToResponseDto(any(Photo.class));
    }
}