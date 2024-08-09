package com.time.demo.service.impl;

import com.time.demo.dto.AuthResponse;
import com.time.demo.entity.Contacts;
import com.time.demo.entity.UserImage;
import com.time.demo.entity.Users;
import com.time.demo.entity.enums.InviteStatus;
import com.time.demo.repository.ContactsRepository;
import com.time.demo.repository.UserImageRepository;
import com.time.demo.repository.UserRepository;
import com.time.demo.service.UserRestService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
    private final ContactsRepository contactsRepository;

    @Autowired
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String host;

    @Override
    public AuthResponse uploadImage(MultipartFile image, String email) throws IOException {
        if (!Objects.requireNonNull(image.getContentType()).split("/")[0].equals("image")) // only images, image/*
            return new AuthResponse("File formati xato", HttpStatus.BAD_REQUEST);
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
        return new AuthResponse("Profil uchun rasm yuklandi", HttpStatus.CREATED);
    }

    @Override
    public AuthResponse inviteFriendByUsername(String username) {
        Optional<Users> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent() && byUsername.get().isEnabled()){
            Contacts contacts=new Contacts();
            contacts.setContact(byUsername.get());
            contacts.setStatus(InviteStatus.WAITING);
            contactsRepository.save(contacts);
            return new AuthResponse("Taklif havolasi yuborildi", HttpStatus.OK);
        }
        return new AuthResponse("Bunday foydalanuvchi nom egasi topilmadi", HttpStatus.BAD_REQUEST);
    }

    @Override
    public AuthResponse sendInvitationLetterToEmail(String email, Users user) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        mimeMessage.setContent("<html><body><h1>Taklif havolasi </h1><h3>" + user + " sizni free.time.uz saytiga taklif qilayabdi</h3> <b>Siz saytga a'zo bo'lish orqali quyidagi imkoniyatlarga ega bo'lasiz:</b><ul><li>Kunlik vazifalarni rejalashtirish</li><li>Biznes hamkorlaringiz va do'stlaringiz bilan uchrashuvlar vaqtini belgilash</li></ul></body></html>", "text/html");
        helper.setTo(email);
        helper.setFrom(host);
        helper.setSubject("Taklif havolasi");
        mailSender.send(mimeMessage);
        return new AuthResponse("Emailga taklif havolasi yuborildi", HttpStatus.OK);
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
