package com.matryoshkaja.demo.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Embeddable
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PhotoCarouselImage {

    @Column(name = "image_url", nullable = false)
    @NotBlank
    private String imageUrl;

    @Column(name = "image_key", nullable = false)
    @NotBlank
    private String imageKey;

    @Column(name = "display_order")
    private Integer displayOrder;
}