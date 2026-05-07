package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import com.matryoshkaja.demo.Storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadPhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private StorageService storageService;

    @Mock
    private PhotoMapper photoMapper;

    @InjectMocks
    private UploadPhotoService uploadPhotoService;

    @Test
    void shouldUploadPhoto() {
        // given
        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("test.png");

        String key = "uuid_test.png";
        String url = "https://fake-storage.com/" + key;
        String caption = "Fresh caption";

        Photo saved = Photo.builder()
                .id(1L)
                .imageKey(key)
                .imageUrl(url)
                .caption(caption)
                .displayOrder(1)
                .build();

        PhotoResponseDto responseDto = PhotoResponseDto.builder()
                .id(1L)
                .imageUrl(url)
                .caption(caption)
                .displayOrder(1)
                .build();

        when(storageService.generateKey("test.png")).thenReturn(key);
        when(storageService.upload(file, key)).thenReturn(url);
        when(photoRepository.findTopByOrderByDisplayOrderDesc()).thenReturn(Optional.empty());
        when(photoRepository.save(any(Photo.class))).thenReturn(saved);
        when(photoMapper.mapEntityToResponseDto(saved)).thenReturn(responseDto);

        // when
        PhotoResponseDto result = uploadPhotoService.uploadPhoto(file, caption);

        // then
        assertNotNull(result);
        assertEquals(url, result.getImageUrl());
        assertEquals(caption, result.getCaption());
        assertEquals(1, result.getDisplayOrder());

        verify(storageService).generateKey("test.png");
        verify(storageService).upload(file, key);
        verify(photoRepository).findTopByOrderByDisplayOrderDesc();
        verify(photoRepository).save(any(Photo.class));
        verify(photoMapper).mapEntityToResponseDto(saved);
    }

    @Test
    void shouldNotUploadPhotoWhenFileIsNull() {
        assertThrows(IllegalArgumentException.class, () -> uploadPhotoService.uploadPhoto(null, "caption"));

        verifyNoInteractions(storageService);
        verifyNoInteractions(photoRepository);
        verifyNoInteractions(photoMapper);
    }

    @Test
    void shouldNotUploadWhenFileIsEmpty() {
        // given
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        // when / then
        assertThrows(IllegalArgumentException.class, () -> uploadPhotoService.uploadPhoto(file, "caption"));

        verify(photoRepository, never()).save(any());
        verifyNoInteractions(storageService);
        verifyNoInteractions(photoMapper);
    }

    @Test
    void shouldSetKeyUrlCaptionAndDisplayOrderOnEntity() {
        // given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.png");

        String key = "uuid_test.png";
        String url = "https://fake-storage.com/" + key;

        Photo lastPhoto = Photo.builder()
                .id(10L)
                .imageUrl("old-url")
                .imageKey("old-key")
                .caption("old caption")
                .displayOrder(7)
                .build();

        when(storageService.generateKey("test.png")).thenReturn(key);
        when(storageService.upload(file, key)).thenReturn(url);
        when(photoRepository.findTopByOrderByDisplayOrderDesc()).thenReturn(Optional.of(lastPhoto));
        when(photoRepository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(photoMapper.mapEntityToResponseDto(any(Photo.class))).thenReturn(new PhotoResponseDto());

        // when
        uploadPhotoService.uploadPhoto(file, "  New caption  ");

        // then
        verify(photoRepository).save(argThat(photo ->
                key.equals(photo.getImageKey()) &&
                        url.equals(photo.getImageUrl()) &&
                        "New caption".equals(photo.getCaption()) &&
                        Integer.valueOf(8).equals(photo.getDisplayOrder())
        ));
    }

    @Test
    void shouldSetEmptyCaptionWhenCaptionIsNull() {
        // given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.png");

        String key = "uuid_test.png";
        String url = "https://fake-storage.com/" + key;

        when(storageService.generateKey("test.png")).thenReturn(key);
        when(storageService.upload(file, key)).thenReturn(url);
        when(photoRepository.findTopByOrderByDisplayOrderDesc()).thenReturn(Optional.empty());
        when(photoRepository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(photoMapper.mapEntityToResponseDto(any(Photo.class))).thenReturn(new PhotoResponseDto());

        // when
        uploadPhotoService.uploadPhoto(file, null);

        // then
        verify(photoRepository).save(argThat(photo ->
                "".equals(photo.getCaption()) &&
                        Integer.valueOf(1).equals(photo.getDisplayOrder())
        ));
    }
}
