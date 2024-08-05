package com.time.demo.controller;

import com.time.demo.dto.ApiResponse;
import com.time.demo.dto.LoginDto;
import com.time.demo.dto.RegisterDto;
import com.time.demo.dto.VerificationDto;
import com.time.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        ApiResponse register = authService.register(registerDto);
        return ResponseEntity.status(register.getHttpStatus()).body(register);
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verification(@RequestBody VerificationDto verificationDto) {
        ApiResponse verification = authService.verification(verificationDto);
        return ResponseEntity.status(verification.getHttpStatus()).body(verification);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        ApiResponse login = authService.login(loginDto);
        return ResponseEntity.status(login.getHttpStatus()).body(login);
    }

    //password-reset
}