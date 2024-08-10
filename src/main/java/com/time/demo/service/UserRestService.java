package com.time.demo.service;

import com.time.demo.dto.ApiResponse;
import com.time.demo.dto.UserDto;
import com.time.demo.entity.Users;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface UserRestService {
    ApiResponse uploadImage(MultipartFile image, String email) throws IOException;

    ApiResponse inviteFriendByUsername(String username);

    ApiResponse sendInvitationLetterToEmail(String email, Users user) throws MessagingException;

    List<UserDto> getUsersByUsername(String username);

    ApiResponse acceptInvitation(Long id, String answer, Users user);
}
