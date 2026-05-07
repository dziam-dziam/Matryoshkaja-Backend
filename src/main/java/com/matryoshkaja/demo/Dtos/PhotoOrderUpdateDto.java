package com.matryoshkaja.demo.Dtos;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PhotoOrderUpdateDto {
    // REORDER CHANGE: ids in the exact order selected by the admin.
    private List<Long> photoIds;
}
