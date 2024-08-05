package com.time.demo.service;

import com.time.demo.dto.ApiResponse;
import com.time.demo.dto.LoginDto;
import com.time.demo.dto.RegisterDto;
import com.time.demo.dto.VerificationDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    ApiResponse register(RegisterDto registerDto);

    ApiResponse verification(VerificationDto verificationDto);

    ApiResponse login(LoginDto loginDto);
}
