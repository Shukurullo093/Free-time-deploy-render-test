package com.time.demo.controller;

import com.time.demo.dto.*;
import com.time.demo.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) throws MessagingException {
        AuthResponse register = authService.register(registerDto);
        return ResponseEntity.status(register.getHttpStatus()).body(register);
    }

    @PostMapping("/verification")
    public ResponseEntity<?> verification(@RequestBody VerificationDto verificationDto) {
        AuthResponse verification = authService.verification(verificationDto);
        return ResponseEntity.status(verification.getHttpStatus()).body(verification);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        AuthResponse login = authService.login(loginDto);
        return ResponseEntity.status(login.getHttpStatus()).body(login);
    }

    //password-reset
}
