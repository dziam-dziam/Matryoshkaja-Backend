package com.matryoshkaja.demo.Dtos;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoCaptionUpdateDto {
    // CAPTION CHANGE: request body for editing the photo caption later.
    private String caption;
}
