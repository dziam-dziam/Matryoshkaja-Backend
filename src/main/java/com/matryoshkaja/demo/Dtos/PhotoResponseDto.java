package com.matryoshkaja.demo.Dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoResponseDto {
    private Long id;

    private String imageUrl;

    // REORDER CHANGE: frontend uses this to keep the current photo order.
    private Integer displayOrder;
}
