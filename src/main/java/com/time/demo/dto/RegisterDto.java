package com.time.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private String password;
}
