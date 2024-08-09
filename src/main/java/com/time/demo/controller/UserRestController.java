package com.time.demo.controller;

import com.time.demo.dto.AuthResponse;
import com.time.demo.entity.UserImage;
import com.time.demo.entity.Users;
import com.time.demo.repository.UserImageRepository;
import com.time.demo.repository.UserRepository;
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
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserRestController {
    private final UserRestService userRestService;
    private final UserImageRepository userImageRepository;
    private final UserRepository userRepository;

    @PostMapping("/upload/image")
    public AuthResponse uploadImage(@RequestParam("image") MultipartFile image, @CurrentUser Users users) throws IOException {
        return userRestService.uploadImage(image, users.getEmail());
    }

    //  must change | -> service impl
    @GetMapping("/avatar/{hashID}")
    public ResponseEntity<?> viewImage(@PathVariable String hashID){
        Optional<UserImage> byHashId = userImageRepository.findByHashId(hashID);
        UserImage userImage = byHashId.get();
        ByteArrayResource byteArrayResource=new ByteArrayResource(userImage.getImageByte());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; fileName=\""+ URLEncoder.encode(userImage.getName()))
                .contentType(MediaType.valueOf(userImage.getContentType()))
                .contentLength(userImage.getFileSize())
                .body(byteArrayResource);
    }

    @PostMapping("/invite-friend-by-username")
    public ResponseEntity<AuthResponse> inviteFriendByUsername(@RequestParam("username") String username){
        AuthResponse invite = userRestService.inviteFriendByUsername(username);
        return ResponseEntity.status(invite.getHttpStatus()).body(invite);
    }

    @PostMapping("/send-invitation-letter-to-email")
    public ResponseEntity<AuthResponse> sendInvitationLetterToEmail(@RequestParam("email") String email, @CurrentUser Users user) throws MessagingException {
        AuthResponse invite = userRestService.sendInvitationLetterToEmail(email, user);
        return ResponseEntity.status(invite.getHttpStatus()).body(invite);
    }
}
