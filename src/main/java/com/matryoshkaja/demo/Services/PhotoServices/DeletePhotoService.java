package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Exceptions.PhotoNotFoundException;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import com.matryoshkaja.demo.Storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletePhotoService {
    private final PhotoRepository photoRepository;
    private final StorageService storageService;

    public void deletePhoto(Long photoId){
        if (photoId == null){
            throw new IllegalArgumentException("Photo Id cannot be null");
        }
        Photo photoToDelete = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException(photoId));

        storageService.delete(photoToDelete.getImageKey());
        photoRepository.delete(photoToDelete);
    }
}
