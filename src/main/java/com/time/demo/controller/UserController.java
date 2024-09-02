package com.time.demo.controller;

import com.time.demo.dto.*;
import com.time.demo.entity.Users;
import com.time.demo.repository.UserRepository;
import com.time.demo.security.CurrentUser;
import com.time.demo.security.SecurityUtils;
import com.time.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(@CurrentUser Users user) {
//        System.out.println(SecurityUtils.getCurrentUser());
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/groups")
    public Map<String, List<GroupDto>> getGroupsList(@CurrentUser Users user) {
        return userService.getGroupsList(user);
    }

    @GetMapping("/contacts")
    public Map<String, List<ContactsDto>> getContactsList(@CurrentUser Users user) {
        return userService.getContactsList(user);
    }

    @GetMapping("/profile")
    public Map<String, ProfileDto> getProfileInfo(@CurrentUser Users user){
        return userService.getProfileInfo(user);
    }
}
