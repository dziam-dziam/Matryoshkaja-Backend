package com.matryoshkaja.demo.Services.PhotoServices;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoOrderUpdateDto;
import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Mappers.PhotoMapper;
import com.matryoshkaja.demo.Repositories.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatePhotoOrderService {
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public List<PhotoResponseDto> updateOrder(PhotoOrderUpdateDto request) {
        if (request == null || request.getPhotoIds() == null || request.getPhotoIds().isEmpty()) {
            throw new IllegalArgumentException("Photo order cannot be empty");
        }

        List<Photo> photos = photoRepository.findAllById(request.getPhotoIds());

        if (photos.size() != request.getPhotoIds().size()) {
            throw new IllegalArgumentException("Photo order contains unknown photo ids");
        }

        // REORDER CHANGE: each photo receives its new position from the admin-selected id list.
        for (Photo photo : photos) {
            int index = request.getPhotoIds().indexOf(photo.getId());
            photo.setDisplayOrder(index + 1);
        }

        photoRepository.saveAll(photos);

        return photoRepository.findAllByOrderByDisplayOrderAscIdAsc()
                .stream()
                .map(photoMapper::mapEntityToResponseDto)
                .toList();
    }
}
