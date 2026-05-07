package com.matryoshkaja.demo.Mappers;

import com.matryoshkaja.demo.Dtos.PhotoDtos.PhotoResponseDto;
import com.matryoshkaja.demo.Entities.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
@AllArgsConstructor
public class PhotoMapper {

    public PhotoResponseDto mapEntityToResponseDto(Photo entity){
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null");
        return PhotoResponseDto.builder()
                .id(entity.getId())
                .imageUrl(entity.getImageUrl())
                // CAPTION CHANGE: pass editable caption to frontend.
                .caption(entity.getCaption())
                .displayOrder(entity.getDisplayOrder())
                .build();
    }

    public Photo mapResponseDtoToEntity(PhotoResponseDto dto){
        if (dto == null) throw new IllegalArgumentException("Dto cannot be null");
        return Photo.builder()
                .id(dto.getId())
                .imageUrl(dto.getImageUrl())
                // CAPTION CHANGE: keep caption when mapping back to entity.
                .caption(dto.getCaption())
                .displayOrder(dto.getDisplayOrder())
                .build();
    }
}
