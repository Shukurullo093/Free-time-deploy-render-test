package com.time.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String groupName;
//    private String email;
//    private String phone;
    private String image; // image-path
    private String createdDate;
    private boolean isSave;
//    private ContactType status;
    private boolean isHolder;
}

