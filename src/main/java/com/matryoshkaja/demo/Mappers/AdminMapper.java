package com.matryoshkaja.demo.Mappers;

import com.matryoshkaja.demo.Dtos.AdminDtos.AdminCreateDto;
import com.matryoshkaja.demo.Dtos.AdminDtos.AdminResponseDto;
import com.matryoshkaja.demo.Entities.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

@Component
@Builder
@AllArgsConstructor
public class AdminMapper {
    public Admin mapCreateDtoToEntity(AdminCreateDto dto){
        if (dto == null){
            throw new IllegalArgumentException("Dto cannot be null");
        }
        return Admin.builder()
                .email(dto.getEmail())
                .build();
    }

    public AdminResponseDto mapEntityToResponseDto(Admin entity){
        if (entity == null){
            throw new IllegalArgumentException("Entity cannot be null");
        }
        return AdminResponseDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .build();
    }
}
