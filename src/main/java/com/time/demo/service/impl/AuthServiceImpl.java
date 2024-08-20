package com.time.demo.service.impl;

import com.time.demo.config.JwtService;
import com.time.demo.dto.*;
import com.time.demo.entity.Users;
import com.time.demo.entity.enums.Roles;
import com.time.demo.repository.UserRepository;
import com.time.demo.service.AuthService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl extends AbsGeneral implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String host;

    @Override
    public AuthResponse register(RegisterDto registerDto) throws MessagingException {
        if (!isValidPassword(registerDto.getPassword()))
            return new AuthResponse("Yaroqsiz parol", HttpStatus.BAD_REQUEST);
        if (userRepository.existsByUsername1(registerDto.getUsername()))
            return new AuthResponse("Bu foydalanuvchi nomi allaqachon foydalanilgan", HttpStatus.BAD_REQUEST);
        if (isValidUserName(registerDto.getUsername()))
            return new AuthResponse("Yaroqsiz foydalanuvchi nomi", HttpStatus.BAD_REQUEST);
        Optional<Users> usersOptional = userRepository.findByEmail(registerDto.getEmail());
        if (usersOptional.isEmpty()) {
            Users users = new Users();
            users.setFirstName(registerDto.getFirstName());
            users.setLastName(registerDto.getLastName());
            users.setUsername1(registerDto.getUsername());
            users.setRole(Roles.USER);
            users.setEmail(registerDto.getEmail());
            users.setPassword(passwordEncoder.encode(registerDto.getPassword()));

            // generate random OTP
            String otp = generateOTP();
            users.setEmailCode(otp);

            if (!sendVerificationCode(registerDto.getEmail(), otp)) {
                return new AuthResponse("Email manzil topilmadi", HttpStatus.BAD_REQUEST);
            }

            userRepository.save(users);
            return new AuthResponse("Foydalanuvchi muvaffaqqiyatli ro'yhatdan o'tdi", HttpStatus.CREATED);
        } else {
            Users users = usersOptional.get();
            if (!users.isEnabled() && users.getEmailCode() != null) {
                //  if before registered but didn't verify email, will send otp again
                String otp = generateOTP();
                users.setEmailCode(otp);
                try {
                    sendVerificationCode(registerDto.getEmail(), otp);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                userRepository.save(users);
                return new AuthResponse("Emailga tasdilash kodi jo'natildi", HttpStatus.OK);
            }
            return new AuthResponse("Bu email allaqachon ro'yhatdan o'tgan", HttpStatus.CONFLICT);
        }
    }

    @Override
    public AuthResponse verification(VerificationDto verificationDto) {
        Optional<Users> usersOptional = userRepository.findByEmail(verificationDto.getEmail());
        if (usersOptional.isEmpty())
            return new AuthResponse("Bunday foydalanuvchi topilmadi", HttpStatus.NOT_FOUND);
        Users users = usersOptional.get();
        if (!users.isEnabled() && users.getEmailCode() != null) {
            if (users.getEmailCode().equals(verificationDto.getCode())) {
                users.setEmailCode(null);
                users.setEnabled(true);
                userRepository.save(users);

                var user = userRepository.findByEmail(users.getEmail())
                        .orElseThrow();
                var jwtToken = jwtService.generateToken(user);

                return new AuthResponse("Email tasdiqlandi", HttpStatus.OK, getUserDtoFromUser(user, jwtToken));
            } else
                return new AuthResponse("Tasdiqlash kodi xato", HttpStatus.BAD_REQUEST);
        }
        return new AuthResponse("Email allaqachon tasdiqlangan", HttpStatus.ALREADY_REPORTED);
    }

    @Override
    public AuthResponse login(LoginDto loginDto) {
        Optional<Users> usersOptional = userRepository.findByEmail(loginDto.getEmail());
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            if (!users.isEnabled() && users.getEmailCode() != null) {
                String otp = generateOTP();
                users.setEmailCode(otp);
                try {
                    sendVerificationCode(loginDto.getEmail(), otp);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                userRepository.save(users);
                return new AuthResponse("Email tasdiqlanmagan, Emailga tasdiqlash kodi yuborildi", HttpStatus.FORBIDDEN);
            }
            if (passwordEncoder.matches(loginDto.getPassword(), users.getPassword())) {
                var user = userRepository.findByEmail(users.getEmail())
                        .orElseThrow();
                var jwtToken = jwtService.generateToken(user);

                return new AuthResponse("Muvaffaqqiyatli tizimga kirdingiz", HttpStatus.OK, getUserDtoFromUser(user, jwtToken));
            } else {
                return new AuthResponse("Email yoki Parol xato", HttpStatus.BAD_REQUEST);
            }
        }
        return new AuthResponse("Ushbu emailli foydalanuvchi topilmadi", HttpStatus.NOT_FOUND);
    }

    private boolean sendVerificationCode(String to, String otp) throws MessagingException {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent("<html><body><h1>Tasdiqlash kodi: </h1><h3>" + otp + "</h3></body></html>", "text/html");
            helper.setTo(to);
            helper.setFrom(host);
            helper.setSubject("Tasdiqlash kodi");
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.getStackTrace();
            return false;
        }
    }

    private static String generateOTP() {
        Random random = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder otp = new StringBuilder();
        while (otp.length() < 6)
            otp.append(characters.charAt(random.nextInt(0, characters.length())));
        return otp.toString();
    }
}

//  https://www.codejava.net/frameworks/spring-boot/spring-security-limit-login-attempts-example
