package com.matryoshkaja.demo.Dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoCarouselImageDto {
    private String imageUrl;
    private Integer displayOrder;
}