package com.time.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
}
