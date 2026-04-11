package com.matryoshkaja.demo.Dtos.AdminDtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCreateDto {

    @Email
    @NotBlank
    private String email;

    @Size(min = 6, max = 25)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=(.*\\d){2,})(?=.*[^A-Za-z\\d]).{6,25}$",
    message = "Password must have between 6-25 characters length, 2 digits and 1 special character")
    private String password;


}
