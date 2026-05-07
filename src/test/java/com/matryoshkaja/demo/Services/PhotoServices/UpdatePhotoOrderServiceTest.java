package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoOrderUpdateDto;
import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePhotoOrderServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private PhotoMapper photoMapper;

    @InjectMocks
    private UpdatePhotoOrderService updatePhotoOrderService;

    @Test
    void shouldUpdatePhotoOrder() {
        // given
        PhotoOrderUpdateDto request = PhotoOrderUpdateDto.builder()
                .photoIds(List.of(3L, 1L, 2L))
                .build();

        Photo photo1 = Photo.builder()
                .id(1L)
                .imageUrl("url1")
                .imageKey("key1")
                .displayOrder(1)
                .build();

        Photo photo2 = Photo.builder()
                .id(2L)
                .imageUrl("url2")
                .imageKey("key2")
                .displayOrder(2)
                .build();

        Photo photo3 = Photo.builder()
                .id(3L)
                .imageUrl("url3")
                .imageKey("key3")
                .displayOrder(3)
                .build();

        List<Photo> foundPhotos = List.of(photo1, photo2, photo3);
        List<Photo> sortedPhotos = List.of(photo3, photo1, photo2);

        PhotoResponseDto dto3 = PhotoResponseDto.builder()
                .id(3L)
                .imageUrl("url3")
                .displayOrder(1)
                .build();

        PhotoResponseDto dto1 = PhotoResponseDto.builder()
                .id(1L)
                .imageUrl("url1")
                .displayOrder(2)
                .build();

        PhotoResponseDto dto2 = PhotoResponseDto.builder()
                .id(2L)
                .imageUrl("url2")
                .displayOrder(3)
                .build();

        when(photoRepository.findAllById(request.getPhotoIds())).thenReturn(foundPhotos);
        when(photoRepository.findAllByOrderByDisplayOrderAscIdAsc()).thenReturn(sortedPhotos);
        when(photoMapper.mapEntityToResponseDto(photo3)).thenReturn(dto3);
        when(photoMapper.mapEntityToResponseDto(photo1)).thenReturn(dto1);
        when(photoMapper.mapEntityToResponseDto(photo2)).thenReturn(dto2);

        // when
        List<PhotoResponseDto> result = updatePhotoOrderService.updateOrder(request);

        // then
        assertNotNull(result);
        assertEquals(3, result.size());

        assertEquals(3L, result.get(0).getId());
        assertEquals(1, result.get(0).getDisplayOrder());

        assertEquals(1L, result.get(1).getId());
        assertEquals(2, result.get(1).getDisplayOrder());

        assertEquals(2L, result.get(2).getId());
        assertEquals(3, result.get(2).getDisplayOrder());

        assertEquals(2, photo1.getDisplayOrder());
        assertEquals(3, photo2.getDisplayOrder());
        assertEquals(1, photo3.getDisplayOrder());

        verify(photoRepository).findAllById(request.getPhotoIds());
        verify(photoRepository).saveAll(foundPhotos);
        verify(photoRepository).findAllByOrderByDisplayOrderAscIdAsc();

        verify(photoMapper).mapEntityToResponseDto(photo3);
        verify(photoMapper).mapEntityToResponseDto(photo1);
        verify(photoMapper).mapEntityToResponseDto(photo2);
    }

    @Test
    void shouldSavePhotosWithUpdatedDisplayOrder() {
        // given
        PhotoOrderUpdateDto request = PhotoOrderUpdateDto.builder()
                .photoIds(List.of(2L, 1L))
                .build();

        Photo photo1 = Photo.builder()
                .id(1L)
                .displayOrder(1)
                .build();

        Photo photo2 = Photo.builder()
                .id(2L)
                .displayOrder(2)
                .build();

        when(photoRepository.findAllById(request.getPhotoIds())).thenReturn(List.of(photo1, photo2));
        when(photoRepository.findAllByOrderByDisplayOrderAscIdAsc()).thenReturn(List.of(photo2, photo1));

        when(photoMapper.mapEntityToResponseDto(photo2)).thenReturn(
                PhotoResponseDto.builder()
                        .id(2L)
                        .displayOrder(1)
                        .build()
        );

        when(photoMapper.mapEntityToResponseDto(photo1)).thenReturn(
                PhotoResponseDto.builder()
                        .id(1L)
                        .displayOrder(2)
                        .build()
        );

        ArgumentCaptor<List<Photo>> photosCaptor = ArgumentCaptor.forClass(List.class);

        // when
        updatePhotoOrderService.updateOrder(request);

        // then
        verify(photoRepository).saveAll(photosCaptor.capture());

        List<Photo> savedPhotos = photosCaptor.getValue();

        Photo savedPhoto1 = savedPhotos.stream()
                .filter(photo -> photo.getId().equals(1L))
                .findFirst()
                .orElseThrow();

        Photo savedPhoto2 = savedPhotos.stream()
                .filter(photo -> photo.getId().equals(2L))
                .findFirst()
                .orElseThrow();

        assertEquals(2, savedPhoto1.getDisplayOrder());
        assertEquals(1, savedPhoto2.getDisplayOrder());
    }

    @Test
    void shouldThrowWhenRequestIsNull() {
        // given null / when / then
        assertThrows(IllegalArgumentException.class,
                () -> updatePhotoOrderService.updateOrder(null));

        verifyNoInteractions(photoRepository);
        verifyNoInteractions(photoMapper);
    }

    @Test
    void shouldThrowWhenPhotoIdsAreNull() {
        // given
        PhotoOrderUpdateDto request = PhotoOrderUpdateDto.builder()
                .photoIds(null)
                .build();

        // when / then
        assertThrows(IllegalArgumentException.class,
                () -> updatePhotoOrderService.updateOrder(request));

        verifyNoInteractions(photoRepository);
        verifyNoInteractions(photoMapper);
    }

    @Test
    void shouldThrowWhenPhotoIdsAreEmpty() {
        // given
        PhotoOrderUpdateDto request = PhotoOrderUpdateDto.builder()
                .photoIds(List.of())
                .build();

        // when / then
        assertThrows(IllegalArgumentException.class,
                () -> updatePhotoOrderService.updateOrder(request));

        verifyNoInteractions(photoRepository);
        verifyNoInteractions(photoMapper);
    }

    @Test
    void shouldThrowWhenPhotoOrderContainsUnknownPhotoIds() {
        // given
        PhotoOrderUpdateDto request = PhotoOrderUpdateDto.builder()
                .photoIds(List.of(1L, 2L, 999L))
                .build();

        Photo photo1 = Photo.builder()
                .id(1L)
                .displayOrder(1)
                .build();

        Photo photo2 = Photo.builder()
                .id(2L)
                .displayOrder(2)
                .build();

        when(photoRepository.findAllById(request.getPhotoIds()))
                .thenReturn(List.of(photo1, photo2));

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> updatePhotoOrderService.updateOrder(request));

        assertEquals("Photo order contains unknown photo ids", exception.getMessage());

        verify(photoRepository).findAllById(request.getPhotoIds());
        verify(photoRepository, never()).saveAll(any());
        verify(photoRepository, never()).findAllByOrderByDisplayOrderAscIdAsc();
        verifyNoInteractions(photoMapper);
    }

    @Test
    void shouldThrowExpectedMessageWhenPhotoOrderIsEmpty() {
        // given
        PhotoOrderUpdateDto request = PhotoOrderUpdateDto.builder()
                .photoIds(List.of())
                .build();

        // when / then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> updatePhotoOrderService.updateOrder(request));

        assertEquals("Photo order cannot be empty", exception.getMessage());

        verifyNoInteractions(photoRepository);
        verifyNoInteractions(photoMapper);
    }
}
