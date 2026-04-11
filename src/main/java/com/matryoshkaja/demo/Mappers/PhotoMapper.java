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
        return PhotoResponseDto.builder()
                .id(entity.getId())
                .imageUrl(entity.getImageUrl())
                .build();
    }
}
