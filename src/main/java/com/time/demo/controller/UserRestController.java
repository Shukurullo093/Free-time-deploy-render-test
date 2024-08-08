package com.time.demo.controller;

import com.time.demo.dto.ApiResponse;
import com.time.demo.entity.UserImage;
import com.time.demo.entity.Users;
import com.time.demo.repository.UserImageRepository;
import com.time.demo.repository.UserRepository;
import com.time.demo.security.CurrentUser;
import com.time.demo.service.UserRestService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.UEncoder;
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
    private final UserRepository userRepository;

    @PostMapping("/upload/image")
    public ApiResponse uploadImage(@RequestParam("image") MultipartFile image, @CurrentUser Users users) throws IOException {
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

    @PostMapping("/invite-friend")
    public ResponseEntity<ApiResponse> inviteFriend(@RequestParam("email") String emailOrUsername){
        ApiResponse invite = userRestService.inviteFriend(emailOrUsername);
        return ResponseEntity.status(invite.getHttpStatus()).body(invite);
    }

    //  only test api, it will remove later
    @GetMapping("/list")
    public List<Users> getUsersList(){
        return userRepository.findAll();
    }
}
