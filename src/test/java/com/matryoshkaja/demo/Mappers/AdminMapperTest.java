package com.matryoshkaja.demo.Mappers;

import com.matryoshkaja.demo.Dtos.AdminDtos.AdminCreateDto;
import com.matryoshkaja.demo.Dtos.AdminDtos.AdminResponseDto;
import com.matryoshkaja.demo.Entities.Admin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminMapperTest {

    private AdminMapper adminMapper;

    @BeforeEach
    void setUp(){
        adminMapper = new AdminMapper();
    }

    @Test
    void shouldMapAdminCreateDtoToEntity(){
        //given
        AdminCreateDto dto = AdminCreateDto.builder()
                .email("email@test.com")
                .password("Password123!")
                .build();
        //when
        Admin entity = adminMapper.mapAdminCreateDtoToEntity(dto);
        //then
        assertNotNull(entity);
        assertEquals(entity.getEmail(),dto.getEmail());
    }

    @Test
    void shouldNotMapAdminCreateDtoToEntityWhenDtoIsNull(){
        //given null & when & then
        assertThrows(IllegalArgumentException.class, () -> {
            adminMapper.mapAdminCreateDtoToEntity(null);
        });
    }

    @Test
    void shouldNotSetIdAndPassword(){
        //given
        AdminCreateDto dto = AdminCreateDto.builder()
                .email("email@test.com")
                .password("Password123!")
                .build();
        //when
        Admin admin = adminMapper.mapAdminCreateDtoToEntity(dto);
        //then
        assertNull(admin.getId());
        assertNull(admin.getHashedPassword());
    }

    @Test
    void shouldCreateNewInstance(){
        //given
        AdminCreateDto dto = AdminCreateDto.builder()
                .email("test@test.com")
                .password("Test12!")
                .build();
        //when
        Admin admin = adminMapper.mapAdminCreateDtoToEntity(dto);
        //then
        assertNotSame(dto,admin);
    }

    @Test
    void shouldMapEntityToAdminResponseDto(){
        //given
        Admin entity = Admin.builder()
                .id(1L)
                .email("test@test.com")
                .build();
        //when
        AdminResponseDto dto = adminMapper.mapEntityToAdminResponseDto(entity);
        //then
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getEmail(), dto.getEmail());
    }

    @Test
    void shouldNotMapEntityToAdminResponseDtoWhenEntityIsNull(){
        //given null & when & then
        assertThrows(IllegalArgumentException.class, () ->{
            adminMapper.mapEntityToAdminResponseDto(null);
        });
    }

}