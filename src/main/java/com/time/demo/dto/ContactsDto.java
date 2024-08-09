package com.time.demo.dto;

import com.time.demo.entity.enums.InviteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsDto {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String image; // image-path
    private InviteStatus status;
}

