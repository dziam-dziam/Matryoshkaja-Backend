package com.matryoshkaja.demo.Services;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Entities.PhotoCarouselImage;
import com.matryoshkaja.demo.Exceptions.PhotoNotFoundException;
import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import com.matryoshkaja.demo.Storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UploadCarouselPhotoService {

    private final PhotoRepository photoRepository;
    private final StorageService storageService;
    private final PhotoMapper photoMapper;

    public PhotoResponseDto uploadCarouselPhoto(Long photoId, MultipartFile file) {
        if (photoId == null) throw new IllegalArgumentException("Photo Id cannot be null");
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("File input cannot be null or empty");

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException(photoId));

        if (photo.getCarouselImages() == null) {
            photo.setCarouselImages(new ArrayList<>());
        }

        String key = storageService.generateKey(file.getOriginalFilename());
        String url = storageService.upload(file, key);

        int nextDisplayOrder = photo.getCarouselImages().stream()
                .map(PhotoCarouselImage::getDisplayOrder)
                .filter(order -> order != null)
                .max(Integer::compareTo)
                .map(order -> order + 1)
                .orElse(1);

        photo.getCarouselImages().add(PhotoCarouselImage.builder()
                .imageUrl(url)
                .imageKey(key)
                .displayOrder(nextDisplayOrder)
                .build());

        return photoMapper.mapEntityToResponseDto(photoRepository.save(photo));
    }
}