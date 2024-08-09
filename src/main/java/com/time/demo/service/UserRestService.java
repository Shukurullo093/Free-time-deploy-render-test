package com.time.demo.service;

import com.time.demo.dto.AuthResponse;
import com.time.demo.entity.Users;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserRestService {
    AuthResponse uploadImage(MultipartFile image, String email) throws IOException;

    AuthResponse inviteFriendByUsername(String username);

    AuthResponse sendInvitationLetterToEmail(String email, Users user) throws MessagingException;
}
