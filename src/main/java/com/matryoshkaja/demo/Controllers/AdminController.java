package com.matryoshkaja.demo.Controllers;

import com.matryoshkaja.demo.Dtos.AdminDtos.AdminCreateDto;
import com.matryoshkaja.demo.Dtos.AdminDtos.AdminResponseDto;
import com.matryoshkaja.demo.Services.AdminServices.CreateAdminService;
import com.matryoshkaja.demo.Services.AdminServices.GetAdminServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final CreateAdminService createAdminService;
    private final GetAdminServices getAdminServices;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminResponseDto create(@Valid @RequestBody AdminCreateDto createDto){
        return createAdminService.createAdmin(createDto);
    }

    @GetMapping("/get/{adminId}")
    public AdminResponseDto get(@PathVariable Long adminId){
        return getAdminServices.getAdmin(adminId);
    }

    @GetMapping("/get_all")
    public List<AdminResponseDto> getAll(){
        return getAdminServices.getAllAdmins();
    }
}
