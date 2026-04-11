package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Dtos.PhotoResponseDto;
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
    void shouldUploadPhoto(){
        //given
        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("test.png");
        String key = "uuid_test.png";
        String url = "https://fake-storage.com/" + key;

        Photo saved  = Photo.builder()
                .id(1L)
                .imageKey(key)
                .imageUrl(url)
                .build();

        PhotoResponseDto responseDto = PhotoResponseDto.builder()
                .id(1L)
                .imageUrl(url)
                .build();

        when(storageService.generateKey("test.png")).thenReturn(key);
        when(storageService.upload(file,key)).thenReturn(url);
        when(photoRepository.save(any(Photo.class))).thenReturn(saved);
        when(photoMapper.mapEntityToResponseDto(saved)).thenReturn(responseDto);

        //when
        PhotoResponseDto result = uploadPhotoService.uploadPhoto(file);

        //then
        assertNotNull(result);
        assertEquals(url, result.getImageUrl());

        verify(storageService).generateKey("test.png");
        verify(storageService).upload(file,key);
        verify(photoRepository).save(any(Photo.class));
        verify(photoMapper).mapEntityToResponseDto(saved);
    }

    @Test
    void shouldNotUploadPhotoWhenFileNotNull(){
        //given null / when / then
        assertThrows(IllegalArgumentException.class, () -> uploadPhotoService.uploadPhoto(null));
    }

    @Test
    void shouldNotUploadWhenFileIsEmpty(){
        //given
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(true);

        //when / then
        assertThrows(IllegalArgumentException.class, () ->
                uploadPhotoService.uploadPhoto(file));

        verify(photoRepository,never()).save(any());
    }

    @Test
    void shouldSetKeyAndUrlOnEntity(){
        //given
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("test.png");

        String key = "uuid_test.png";
        String url = "https://fake-storage.com/" + key;

        when(storageService.generateKey("test.png")).thenReturn(key);
        when(storageService.upload(file,key)).thenReturn(url);

        when(photoRepository.save(any(Photo.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(photoMapper.mapEntityToResponseDto(any(Photo.class))).thenReturn(new PhotoResponseDto());
        //when
        uploadPhotoService.uploadPhoto(file);

        //then
        verify(photoRepository).save(argThat(photo ->
                key.equals(photo.getImageKey()) &&
                url.equals(photo.getImageUrl())
                ));
    }


}