package com.time.demo.controller;

import com.time.demo.dto.*;
import com.time.demo.entity.Users;
import com.time.demo.security.CurrentUser;
import com.time.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(@CurrentUser Users user) {
        return ResponseEntity.ok("");
    }

    @GetMapping("/contacts")
    public Map<String, List<ContactsDto>> getContactsList(@CurrentUser Users user) {
        return userService.getContactsList(user);
    }
}
