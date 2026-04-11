package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Dtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import com.matryoshkaja.demo.Storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadPhotoService {

    private final PhotoRepository photoRepository;
    private final StorageService storageService;
    private final PhotoMapper photoMapper;

    public PhotoResponseDto uploadPhoto(MultipartFile file){
        if (file == null || file.isEmpty()){
            throw new IllegalArgumentException("File input cannot be null or empty");
        }
        String key = storageService.generateKey(file.getOriginalFilename());

        String url = storageService.upload(file, key);

        Photo newPhoto = Photo.builder()
                .imageUrl(url)
                .imageKey(key)
                .build();

        Photo saved = photoRepository.save(newPhoto);

        return photoMapper.mapEntityToResponseDto(saved);
    }
}
