package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Exceptions.PhotoNotFoundException;
import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetPhotoService {

    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public PhotoResponseDto getPhoto(Long photoId){
        if (photoId == null){
            throw new IllegalArgumentException("Photo Id cannot be null");
        }

        Photo photo = photoRepository.findById(photoId)
                .orElseThrow(() -> new PhotoNotFoundException(photoId));

        return photoMapper.mapEntityToResponseDto(photo);
    }

    public List<PhotoResponseDto> getAllPhotos(){
        // REORDER CHANGE: photos are always returned in the saved lookbook order.
        return photoRepository.findAllByOrderByDisplayOrderAscIdAsc()
                .stream()
                .map(photoMapper::mapEntityToResponseDto)
                .toList();
    }
}
