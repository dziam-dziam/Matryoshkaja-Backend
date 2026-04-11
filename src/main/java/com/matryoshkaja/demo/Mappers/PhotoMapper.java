package com.matryoshkaja.demo.Mappers;

import com.matryoshkaja.demo.Dtos.PhotoResponseDto;
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
                .build();
    }

    public Photo mapResponseDtoToEntity(PhotoResponseDto dto){
        if (dto == null) throw new IllegalArgumentException("Dto cannot be null");
        return Photo.builder()
                .id(dto.getId())
                .imageUrl(dto.getImageUrl())
                .build();
    }
}
