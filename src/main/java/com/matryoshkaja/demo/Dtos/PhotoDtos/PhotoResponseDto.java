package com.matryoshkaja.demo.Dtos.PhotoDtos;

import lombok.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoResponseDto {
    private Long id;

    private String imageUrl;

    private String caption;

    private Integer displayOrder;

    private List<PhotoCarouselImageDto> carouselImages;
}
