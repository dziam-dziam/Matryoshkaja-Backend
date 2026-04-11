package com.matryoshkaja.demo.Services.AdminServices;

import com.matryoshkaja.demo.Dtos.AdminDtos.AdminCreateDto;
import com.matryoshkaja.demo.Dtos.AdminDtos.AdminResponseDto;
import com.matryoshkaja.demo.Entities.Admin;
import com.matryoshkaja.demo.Mappers.AdminMapper;
import com.matryoshkaja.demo.Repositories.AdminRepository;
import com.matryoshkaja.demo.Security.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateAdminService {
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final PasswordService passwordService;

    public AdminResponseDto createAdmin(AdminCreateDto dto){
        if (dto == null) throw new IllegalArgumentException("CreateDto cannot be null");
        Admin newAdmin = adminMapper.mapCreateDtoToEntity(dto);
        String hashedPassword = passwordService.hash(dto.getPassword());
        newAdmin.setHashedPassword(hashedPassword);

        Admin saved = adminRepository.save(newAdmin);
        return adminMapper.mapEntityToResponseDto(saved);
    }
}
