package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoCaptionUpdateDto;
import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Exceptions.PhotoNotFoundException;
import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdatePhotoCaptionService {
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public PhotoResponseDto updateCaption(Long photoId, PhotoCaptionUpdateDto request) {
        if (photoId == null) {
            throw new IllegalArgumentException("Photo Id cannot be null");
        }

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException(photoId));

        // CAPTION CHANGE: empty captions are allowed, so admin can clear the text.
        photo.setCaption(request == null || request.getCaption() == null ? "" : request.getCaption().trim());

        return photoMapper.mapEntityToResponseDto(photoRepository.save(photo));
    }
}
