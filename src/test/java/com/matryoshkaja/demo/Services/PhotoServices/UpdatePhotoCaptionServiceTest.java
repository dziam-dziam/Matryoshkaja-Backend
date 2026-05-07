package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoCaptionUpdateDto;
import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Exceptions.PhotoNotFoundException;
import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePhotoCaptionServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private PhotoMapper photoMapper;

    @InjectMocks
    private UpdatePhotoCaptionService updatePhotoCaptionService;

    @Test
    void shouldUpdateCaption() {
        // given
        Long photoId = 1L;

        PhotoCaptionUpdateDto request = PhotoCaptionUpdateDto.builder()
                .caption("New caption")
                .build();

        Photo photo = Photo.builder()
                .id(photoId)
                .imageUrl("url")
                .imageKey("key")
                .caption("Old caption")
                .displayOrder(1)
                .build();

        Photo savedPhoto = Photo.builder()
                .id(photoId)
                .imageUrl("url")
                .imageKey("key")
                .caption("New caption")
                .displayOrder(1)
                .build();

        PhotoResponseDto responseDto = PhotoResponseDto.builder()
                .id(photoId)
                .imageUrl("url")
                .caption("New caption")
                .displayOrder(1)
                .build();

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(photoRepository.save(photo)).thenReturn(savedPhoto);
        when(photoMapper.mapEntityToResponseDto(savedPhoto)).thenReturn(responseDto);

        // when
        PhotoResponseDto result = updatePhotoCaptionService.updateCaption(photoId, request);

        // then
        assertNotNull(result);
        assertEquals(photoId, result.getId());
        assertEquals("url", result.getImageUrl());
        assertEquals("New caption", result.getCaption());
        assertEquals(1, result.getDisplayOrder());

        verify(photoRepository).findById(photoId);
        verify(photoRepository).save(photo);
        verify(photoMapper).mapEntityToResponseDto(savedPhoto);
    }

    @Test
    void shouldTrimCaptionBeforeSaving() {
        // given
        Long photoId = 1L;

        PhotoCaptionUpdateDto request = PhotoCaptionUpdateDto.builder()
                .caption("   Trimmed caption   ")
                .build();

        Photo photo = Photo.builder()
                .id(photoId)
                .caption("Old caption")
                .build();

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(photoRepository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(photoMapper.mapEntityToResponseDto(any(Photo.class))).thenReturn(
                PhotoResponseDto.builder()
                        .id(photoId)
                        .caption("Trimmed caption")
                        .build()
        );

        ArgumentCaptor<Photo> photoCaptor = ArgumentCaptor.forClass(Photo.class);

        // when
        PhotoResponseDto result = updatePhotoCaptionService.updateCaption(photoId, request);

        // then
        assertNotNull(result);
        assertEquals("Trimmed caption", result.getCaption());

        verify(photoRepository).save(photoCaptor.capture());
        assertEquals("Trimmed caption", photoCaptor.getValue().getCaption());
    }

    @Test
    void shouldSetEmptyCaptionWhenRequestIsNull() {
        // given
        Long photoId = 1L;

        Photo photo = Photo.builder()
                .id(photoId)
                .caption("Old caption")
                .build();

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(photoRepository.save(photo)).thenReturn(photo);
        when(photoMapper.mapEntityToResponseDto(photo)).thenReturn(
                PhotoResponseDto.builder()
                        .id(photoId)
                        .caption("")
                        .build()
        );

        // when
        PhotoResponseDto result = updatePhotoCaptionService.updateCaption(photoId, null);

        // then
        assertNotNull(result);
        assertEquals("", result.getCaption());
        assertEquals("", photo.getCaption());

        verify(photoRepository).findById(photoId);
        verify(photoRepository).save(photo);
        verify(photoMapper).mapEntityToResponseDto(photo);
    }

    @Test
    void shouldSetEmptyCaptionWhenCaptionIsNull() {
        // given
        Long photoId = 1L;

        PhotoCaptionUpdateDto request = PhotoCaptionUpdateDto.builder()
                .caption(null)
                .build();

        Photo photo = Photo.builder()
                .id(photoId)
                .caption("Old caption")
                .build();

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(photoRepository.save(photo)).thenReturn(photo);
        when(photoMapper.mapEntityToResponseDto(photo)).thenReturn(
                PhotoResponseDto.builder()
                        .id(photoId)
                        .caption("")
                        .build()
        );

        // when
        PhotoResponseDto result = updatePhotoCaptionService.updateCaption(photoId, request);

        // then
        assertNotNull(result);
        assertEquals("", result.getCaption());
        assertEquals("", photo.getCaption());

        verify(photoRepository).findById(photoId);
        verify(photoRepository).save(photo);
        verify(photoMapper).mapEntityToResponseDto(photo);
    }

    @Test
    void shouldSetEmptyCaptionWhenCaptionIsBlank() {
        // given
        Long photoId = 1L;

        PhotoCaptionUpdateDto request = PhotoCaptionUpdateDto.builder()
                .caption("      ")
                .build();

        Photo photo = Photo.builder()
                .id(photoId)
                .caption("Old caption")
                .build();

        when(photoRepository.findById(photoId)).thenReturn(Optional.of(photo));
        when(photoRepository.save(photo)).thenReturn(photo);
        when(photoMapper.mapEntityToResponseDto(photo)).thenReturn(
                PhotoResponseDto.builder()
                        .id(photoId)
                        .caption("")
                        .build()
        );

        // when
        PhotoResponseDto result = updatePhotoCaptionService.updateCaption(photoId, request);

        // then
        assertNotNull(result);
        assertEquals("", result.getCaption());
        assertEquals("", photo.getCaption());

        verify(photoRepository).findById(photoId);
        verify(photoRepository).save(photo);
        verify(photoMapper).mapEntityToResponseDto(photo);
    }

    @Test
    void shouldThrowWhenPhotoIdIsNull() {
        // given
        PhotoCaptionUpdateDto request = PhotoCaptionUpdateDto.builder()
                .caption("Caption")
                .build();

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> updatePhotoCaptionService.updateCaption(null, request));

        assertEquals("Photo Id cannot be null", exception.getMessage());

        verifyNoInteractions(photoRepository);
        verifyNoInteractions(photoMapper);
    }

    @Test
    void shouldThrowWhenPhotoIsNotFound() {
        // given
        Long photoId = 1L;

        PhotoCaptionUpdateDto request = PhotoCaptionUpdateDto.builder()
                .caption("New caption")
                .build();

        when(photoRepository.findById(photoId)).thenReturn(Optional.empty());

        // when / then
        assertThrows(PhotoNotFoundException.class,
                () -> updatePhotoCaptionService.updateCaption(photoId, request));

        verify(photoRepository).findById(photoId);
        verify(photoRepository, never()).save(any(Photo.class));
        verifyNoInteractions(photoMapper);
    }
}
