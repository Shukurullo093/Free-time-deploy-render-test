package com.time.demo.service.impl;

import com.time.demo.dto.ApiResponse;
import com.time.demo.entity.UserImage;
import com.time.demo.entity.Users;
import com.time.demo.repository.UserImageRepository;
import com.time.demo.repository.UserRepository;
import com.time.demo.service.UserRestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRestServiceImpl implements UserRestService {
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;

    @Override
    public ApiResponse uploadImage(MultipartFile image, String email) throws IOException {
        if (!Objects.requireNonNull(image.getContentType()).split("/")[0].equals("image")) // only images, image/*
            return new ApiResponse("File formati xato", HttpStatus.BAD_REQUEST);
        Optional<Users> usersOptional = userRepository.findByEmail(email);
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            UserImage usersImage;
            if (users.getImage() != null) {
                Optional<UserImage> imageOptional = userImageRepository.findById(users.getImage().getId());
                usersImage = imageOptional.orElseGet(UserImage::new);
            } else usersImage = new UserImage();

            usersImage.setName(image.getOriginalFilename());
            usersImage.setExtension(getExtension(image.getOriginalFilename()));
            String encodedString = Base64.getEncoder().encodeToString(Objects.requireNonNull(image.getOriginalFilename()).getBytes());
            usersImage.setHashId(encodedString.substring(0, encodedString.length() - 2));
            usersImage.setFileSize(image.getSize());
            usersImage.setContentType(image.getContentType());
            usersImage.setImageByte(image.getBytes());
            UserImage savedImage = userImageRepository.save(usersImage);

            users.setImage(savedImage);
            userRepository.save(users);
        }
        return new ApiResponse("Profil uchun rasm yuklandi", HttpStatus.CREATED);
    }

    private String getExtension(String fileName) {
        String ext = null;
        if (fileName != null && !fileName.isEmpty()) {
            int dot = fileName.lastIndexOf(".");
            if (dot > 0 && dot <= fileName.length() - 2) {
                ext = fileName.substring(dot + 1);
            }
        }
        return ext;
    }
}
