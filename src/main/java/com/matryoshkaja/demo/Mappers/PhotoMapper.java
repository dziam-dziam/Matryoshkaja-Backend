package com.matryoshkaja.demo.Mappers;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoCarouselImageDto;
import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import com.matryoshkaja.demo.Entities.PhotoCarouselImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;

@Component
@Builder
@AllArgsConstructor
public class PhotoMapper {

    public PhotoResponseDto mapEntityToResponseDto(Photo entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null");

        return PhotoResponseDto.builder()
                .id(entity.getId())
                .imageUrl(entity.getImageUrl())
                .caption(entity.getCaption())
                .displayOrder(entity.getDisplayOrder())
                .carouselImages(entity.getCarouselImages() == null
                        ? new ArrayList<>()
                        : entity.getCarouselImages().stream()
                        .sorted(Comparator.comparing(
                                PhotoCarouselImage::getDisplayOrder,
                                Comparator.nullsLast(Integer::compareTo)
                        ))
                        .map(carouselImage -> PhotoCarouselImageDto.builder()
                                .imageUrl(carouselImage.getImageUrl())
                                .displayOrder(carouselImage.getDisplayOrder())
                                .build())
                        .toList())
                .build();
    }

    public Photo mapResponseDtoToEntity(PhotoResponseDto dto) {
        if (dto == null) throw new IllegalArgumentException("Dto cannot be null");

        return Photo.builder()
                .id(dto.getId())
                .imageUrl(dto.getImageUrl())
                .caption(dto.getCaption())
                .displayOrder(dto.getDisplayOrder())
                .carouselImages(dto.getCarouselImages() == null
                        ? new ArrayList<>()
                        : dto.getCarouselImages().stream()
                        .map(carouselImage -> PhotoCarouselImage.builder()
                                .imageUrl(carouselImage.getImageUrl())
                                .displayOrder(carouselImage.getDisplayOrder())
                                .build())
                        .toList())
                .build();
    }
}