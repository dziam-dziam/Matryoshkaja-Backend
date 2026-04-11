package com.matryoshkaja.demo.Dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoResponseDto {
    private Long id;

    private String imageUrl;
}
