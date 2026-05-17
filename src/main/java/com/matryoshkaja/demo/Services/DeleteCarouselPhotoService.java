package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Entities.PhotoCarouselImage;
import com.matryoshkaja.demo.Exceptions.PhotoNotFoundException;
import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import com.matryoshkaja.demo.Storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteCarouselPhotoService {

    private final PhotoRepository photoRepository;
    private final StorageService storageService;
    private final PhotoMapper photoMapper;

    public PhotoResponseDto deleteCarouselPhoto(Long photoId, Integer displayOrder) {
        if (photoId == null) throw new IllegalArgumentException("Photo Id cannot be null");
        if (displayOrder == null) throw new IllegalArgumentException("Display order cannot be null");

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException(photoId));

        List<PhotoCarouselImage> carouselImages = photo.getCarouselImages();

        if (carouselImages == null || carouselImages.isEmpty()) {
            throw new IllegalArgumentException("Carousel photo not found");
        }

        PhotoCarouselImage imageToDelete = carouselImages.stream()
                .filter(image -> displayOrder.equals(image.getDisplayOrder()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Carousel photo not found"));

        carouselImages.remove(imageToDelete);
        storageService.delete(imageToDelete.getImageKey());
        reorderCarouselImages(carouselImages);

        return photoMapper.mapEntityToResponseDto(photoRepository.save(photo));
    }

    private void reorderCarouselImages(List<PhotoCarouselImage> carouselImages) {
        carouselImages.sort(Comparator.comparing(
                PhotoCarouselImage::getDisplayOrder,
                Comparator.nullsLast(Integer::compareTo)
        ));

        for (int index = 0; index < carouselImages.size(); index += 1) {
            carouselImages.get(index).setDisplayOrder(index + 1);
        }
    }
}