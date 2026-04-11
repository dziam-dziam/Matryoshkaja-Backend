package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Exceptions.PhotoNotFoundException;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import com.matryoshkaja.demo.Storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;


import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletePhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;
    @Mock
    private StorageService storageService;
    @InjectMocks
    DeletePhotoService deletePhotoService;

    @Test
    void shouldDeletePhoto(){
        //given
        Photo photo = Photo.builder()
                .id(1L)
                .imageUrl("url")
                .imageKey("key")
                .build();
        when(photoRepository.findById(1L)).thenReturn(Optional.ofNullable(photo));
        //when
        deletePhotoService.deletePhoto(1L);

        //then
        assertNotNull(photo);
        verify(photoRepository).delete(photo);
        verify(storageService).delete("key");
    }

    @Test
    void shouldNotDeletePhotoWhenIdIsNull(){
        //given null / when / then
        assertThrows(IllegalArgumentException.class,
                () ->deletePhotoService.deletePhoto(null));
    }

    @Test
    void shouldThrowWhenPhotoNotFound(){
        //given
        Long photoId = 1L;
        when(photoRepository.findById(photoId))
                .thenReturn(Optional.empty());

        //when / then
        assertThrows(PhotoNotFoundException.class,
                () -> deletePhotoService.deletePhoto(photoId));

        verify(storageService, never()).delete(any());
        verify(photoRepository, never()).delete(any(Photo.class));
    }

    @Test
    void shouldDeleteFromStorageBeforeRepository(){
        //given
        Photo photo = Photo.builder()
                .id(1L)
                .imageKey("key")
                .imageUrl("url")
                .build();

        when(photoRepository.findById(1L)).thenReturn(Optional.of(photo));

        //when
        deletePhotoService.deletePhoto(1L);

        //then
        InOrder inOrder = inOrder(storageService, photoRepository);
        inOrder.verify(storageService).delete("key");
        inOrder.verify(photoRepository).delete(photo);
    }

}