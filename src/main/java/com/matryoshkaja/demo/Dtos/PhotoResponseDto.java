package com.matryoshkaja.demo.Dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoResponseDto {
    private Long id;

    private String imageUrl;

    // CAPTION CHANGE: frontend displays and edits this text under the photo.
    private String caption;

    // REORDER CHANGE: frontend uses this to keep the current photo order.
    private Integer displayOrder;
}
