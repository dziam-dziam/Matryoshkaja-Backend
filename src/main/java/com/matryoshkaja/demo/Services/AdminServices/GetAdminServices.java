package com.matryoshkaja.demo.Services.AdminServices;

import com.matryoshkaja.demo.Dtos.AdminDtos.AdminResponseDto;
import com.matryoshkaja.demo.Entities.Admin;
import com.matryoshkaja.demo.Exceptions.AdminNotFoundException;
import com.matryoshkaja.demo.Mappers.AdminMapper;
import com.matryoshkaja.demo.Repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAdminServices {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public AdminResponseDto getAdmin(Long adminId){
        if (adminId == null){
            throw new IllegalArgumentException("Admin Id cannot be null");
        }

        Admin admin =  adminRepository.findById(adminId).orElseThrow(
                () -> new AdminNotFoundException(adminId)
        );
        return adminMapper.mapEntityToResponseDto(admin);
    }

    public List<AdminResponseDto> getAllAdmins(){
        return adminRepository.findAll().stream()
                .map(adminMapper::mapEntityToResponseDto)
                .toList();

    }

}
