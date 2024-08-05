package com.time.demo.service;

import com.time.demo.dto.ApiResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserRestService {
    ApiResponse uploadImage(MultipartFile image, String email) throws IOException;
}
