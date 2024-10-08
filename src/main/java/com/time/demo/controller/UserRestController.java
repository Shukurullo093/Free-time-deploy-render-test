package com.time.demo.controller;

import com.time.demo.dto.*;
import com.time.demo.entity.UserImage;
import com.time.demo.entity.Users;
import com.time.demo.repository.UserImageRepository;
import com.time.demo.security.CurrentUser;
import com.time.demo.service.UserRestService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
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
    public ApiResponse uploadImage(@RequestParam("image") MultipartFile image,
                                   @CurrentUser Users users) throws IOException {
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

    @PostMapping("/create-group")
    public ResponseEntity<ApiResponse> createGroup(@CurrentUser Users user, @Valid @RequestBody CreateGroupDto createGroupDto){
        ApiResponse response = userRestService.createGroup(user, createGroupDto.getName(), createGroupDto.getCategory());
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @DeleteMapping("/delete-group/{groupId}")
    public ResponseEntity<ApiResponse> deleteGroup(@CurrentUser Users user, @PathVariable long groupId){
        ApiResponse response = userRestService.deleteGroup(user, groupId);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/get-users-by-username/{username}")
    public List<UserDto> getUsersByUsername(@PathVariable String username, @CurrentUser Users user) {
        return userRestService.getUsersByUsername(username, user);
    }

    // https://medium.com/@tericcabrel/validate-request-body-and-parameter-in-spring-boot-53ca77f97fe9
    @PostMapping("/invite-friend-by-username")
    public ResponseEntity<ApiResponse> sendJoinRequest(@CurrentUser Users user,
                                                       @Valid @RequestBody InviteDto inviteDto) {
        ApiResponse response = userRestService.sendJoinRequest(user, inviteDto.getUsername(), inviteDto.getMessage());
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PostMapping("/send-invitation-letter-to-email")
    public ResponseEntity<ApiResponse> sendInvitationLetterToEmail(@RequestParam("email") String email, @CurrentUser Users user) throws MessagingException {
        ApiResponse response = userRestService.sendInvitationLetterToEmail(email, user);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PostMapping("/accept-invitation")
    public ResponseEntity<ApiResponse> addUserToContactOrGroup(@RequestBody AcceptInvitationDto invitationDto,
                                                                @CurrentUser Users user) {
        ApiResponse response = userRestService.addUserToContactOrGroup(invitationDto.getUsername(), invitationDto.getGroupId(),
                invitationDto.isSave(), user);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PostMapping("/update-contact/{contactId}")
    public ResponseEntity<ApiResponse> updateUserContact(@PathVariable Long contactId,
                                                         @RequestParam("groupId")Long groupId,
                                                         @RequestParam("save")boolean save,
                                                         @CurrentUser Users user) {
        ApiResponse response = userRestService.updateUserContact(contactId, groupId, save, user);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @DeleteMapping("/delete/contact/{id}")
    public ResponseEntity<ApiResponse> deleteContact(@PathVariable Long id, @CurrentUser Users user) {
        ApiResponse response = userRestService.deleteContact(id, user);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @GetMapping("/profile-info")
    public UserDto getProfileInfo(@CurrentUser Users user) {
        return userRestService.getProfileInfo(user);
    }

    @PutMapping("/update/profile")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody ProfileDto profileDto, @CurrentUser Users user) {
        ApiResponse response = userRestService.updateProfile(profileDto, user);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }

    @PutMapping("/update/password")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody PasswordDto passwordDto, @CurrentUser Users user){
        ApiResponse response = userRestService.updatePassword(passwordDto, user);
        return ResponseEntity.status(response.getResponseCode()).body(response);
    }
}
