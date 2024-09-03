package com.time.demo.service;

import com.time.demo.dto.*;
import com.time.demo.entity.Users;
import jakarta.mail.MessagingException;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface UserRestService {
    ApiResponse uploadImage(MultipartFile image, String email) throws IOException;

    ApiResponse sendJoinRequest(Users user, String username, String body);

    ApiResponse sendInvitationLetterToEmail(String email, Users user) throws MessagingException;

    List<UserDto> getUsersByUsername(String username, Users user);

    ApiResponse addUserToContactOrGroup(@NotBlank String userId, Long groupId, boolean save, Users user);

    UserDto getProfileInfo(Users user);

    ApiResponse deleteContact(Long id, Users user);

    ApiResponse updateProfile(ProfileDto profileDto, Users user);

    ApiResponse updatePassword(PasswordDto passwordDto, Users user);

    ApiResponse createGroup(Users user, String name, String category);

    ApiResponse deleteGroup(Users user, long groupId);

    ApiResponse updateUserContact(Long contactId, Long groupId, boolean save, Users user);
}
