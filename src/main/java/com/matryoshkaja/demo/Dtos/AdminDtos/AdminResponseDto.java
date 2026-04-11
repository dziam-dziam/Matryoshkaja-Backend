package com.matryoshkaja.demo.Dtos.AdminDtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDto {
    private Long id;
    private String email;
}
