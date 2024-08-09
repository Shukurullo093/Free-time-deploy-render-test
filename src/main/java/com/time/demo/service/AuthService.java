package com.time.demo.service;

import com.time.demo.dto.AuthResponse;
import com.time.demo.dto.LoginDto;
import com.time.demo.dto.RegisterDto;
import com.time.demo.dto.VerificationDto;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    AuthResponse register(RegisterDto registerDto) throws MessagingException;

    AuthResponse verification(VerificationDto verificationDto);

    AuthResponse login(LoginDto loginDto);
}
