package com.time.demo.controller;

import com.time.demo.dto.ApiResponse;
import com.time.demo.dto.PasswordDto;
import com.time.demo.dto.ProfileDto;
import com.time.demo.dto.UserDto;
import com.time.demo.entity.UserImage;
import com.time.demo.entity.Users;
import com.time.demo.repository.UserImageRepository;
import com.time.demo.security.CurrentUser;
import com.time.demo.service.UserRestService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserRestService userRestService;
    private final UserImageRepository userImageRepository;

    @PostMapping("/upload/image")
    public ApiResponse uploadImage(@RequestParam("image") MultipartFile image, @CurrentUser Users users) throws IOException {
        return userRestService.uploadImage(image, users.getEmail());
    }

    //  must change | -> service impl
    @GetMapping("/avatar/{hashID}")
    public ResponseEntity<?> viewImage(@PathVariable String hashID) {
        Optional<UserImage> byHashId = userImageRepository.findByHashId(hashID);
        UserImage userImage = byHashId.get();
        ByteArrayResource byteArrayResource = new ByteArrayResource(userImage.getImageByte());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\"" + URLEncoder.encode(userImage.getName()))
                .contentType(MediaType.valueOf(userImage.getContentType()))
                .contentLength(userImage.getFileSize())
                .body(byteArrayResource);
    }

    @GetMapping("/get-users-by-username/{username}")
    public List<UserDto> getUsersByUsername(@PathVariable String username) {
        return userRestService.getUsersByUsername(username);
    }

    @PostMapping("/invite-friend-by-username")
    public ResponseEntity<ApiResponse> inviteFriendByUsername(@RequestParam("username") String username) {
        ApiResponse invite = userRestService.inviteFriendByUsername(username);
        return ResponseEntity.status(invite.getHttpStatus()).body(invite);
    }

    @PostMapping("/send-invitation-letter-to-email")
    public ResponseEntity<ApiResponse> sendInvitationLetterToEmail(@RequestParam("email") String email, @CurrentUser Users user) throws MessagingException {
        ApiResponse invite = userRestService.sendInvitationLetterToEmail(email, user);
        return ResponseEntity.status(invite.getHttpStatus()).body(invite);
    }

    @PostMapping("/accept-invitation")
    public ResponseEntity<ApiResponse> acceptInvitation(@RequestParam("id") Long id, @RequestParam("answer") String answer, @CurrentUser Users user) {
        ApiResponse invite = userRestService.acceptInvitation(id, answer, user);
        return ResponseEntity.status(invite.getHttpStatus()).body(invite);
    }

    @DeleteMapping("/delete/invitation/{id}")
    public ResponseEntity<ApiResponse> deleteInvitation(@PathVariable Long id, @CurrentUser Users user) {
        ApiResponse delete = userRestService.deleteInvitation(id, user);
        return ResponseEntity.status(delete.getHttpStatus()).body(delete);
    }

    @GetMapping("/profile-info")
    public UserDto getProfileInfo(@CurrentUser Users user) {
        return userRestService.getProfileInfo(user);
    }

    @PutMapping("/update/profile")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody ProfileDto profileDto, @CurrentUser Users user) {
        ApiResponse apiResponse = userRestService.updateProfile(profileDto, user);
        return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
    }

    @PutMapping("/update/password")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody PasswordDto passwordDto, @CurrentUser Users user){
        ApiResponse apiResponse = userRestService.updatePassword(passwordDto, user);
        return ResponseEntity.status(apiResponse.getHttpStatus()).body(apiResponse);
    }
}
