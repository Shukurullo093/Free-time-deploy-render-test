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
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public ApiResponse register(RegisterDto registerDto) {
        Optional<Users> usersOptional = userRepository.findByEmail(registerDto.getEmail());
        if (usersOptional.isEmpty()) {
            if (!isValidPassword(registerDto.getPassword()))
                return new ApiResponse("Yaroqsiz parol", HttpStatus.BAD_REQUEST);

            Users users = new Users();
            users.setFirstName(registerDto.getFirstName());
            users.setLastName(registerDto.getLastName());
            users.setRole(Roles.USER);
            users.setEmail(registerDto.getEmail());
            users.setPassword(passwordEncoder.encode(registerDto.getPassword()));

            // generate random OTP
            String otp = generateOTP();
            users.setEmailCode(otp);
            try {
                sendVerificationCode(registerDto.getEmail(), otp);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            userRepository.save(users);
            return new ApiResponse("Foydalanuvchi muvaffaqqiyatli ro'yhatdan o'tdi", HttpStatus.CREATED);
        }
        return new ApiResponse("Bu email allaqachon ro'yhatdan o'tgan", HttpStatus.CONFLICT);
    }

    @Override
    public ApiResponse verification(VerificationDto verificationDto) {
        Optional<Users> usersOptional = userRepository.findByEmail(verificationDto.getEmail());
        if (usersOptional.isEmpty())
            return new ApiResponse("Bunday foydalanuvchi topilmadi", HttpStatus.NOT_FOUND);
        Users users = usersOptional.get();
        if (!users.isEnabled() && users.getEmailCode() != null) {
            if (users.getEmailCode().equals(verificationDto.getCode())) {
                users.setEmailCode(null);
                users.setEnabled(true);
                userRepository.save(users);

                var user = userRepository.findByEmail(users.getEmail())
                        .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                UserDto userDto=new UserDto(
                        user.getFirstName(),
                        users.getLastName(),
                        user.getEmail(),
                        "",
                        jwtToken
                );
                return new ApiResponse("Email tasdiqlandi", HttpStatus.OK, userDto);
            } else
                return new ApiResponse("Tasdiqlash kodi xato", HttpStatus.BAD_REQUEST);
        }
        return new ApiResponse("Email allaqachon tasdiqlangan", HttpStatus.ALREADY_REPORTED);
    }

    @Override
    public ApiResponse login(LoginDto loginDto) {
        Optional<Users> usersOptional = userRepository.findByEmail(loginDto.getEmail());
        if (usersOptional.isPresent()){
            Users users = usersOptional.get();
            if (!users.isEnabled() && users.getEmailCode()!=null){
                String otp = generateOTP();
                users.setEmailCode(otp);
                try {
                    sendVerificationCode(loginDto.getEmail(), otp);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                userRepository.save(users);
                return new ApiResponse("Email tasdiqlanmagan, Emailga tasdiqlash kodi yuborildi", HttpStatus.FORBIDDEN);
            }
            if (passwordEncoder.matches(loginDto.getPassword(), users.getPassword())){
                var user = userRepository.findByEmail(users.getEmail())
                        .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                UserDto userDto=new UserDto(
                        user.getFirstName(),
                        users.getLastName(),
                        user.getEmail(),
                        "",
                        jwtToken
                );
                return new ApiResponse("Muvaffaqqiyatli tizimga kirdingiz", HttpStatus.OK, userDto);
            } else {
                return new ApiResponse("Email yoki Parol xato", HttpStatus.BAD_REQUEST);
            }
        }
        return new ApiResponse("Ushbu emailli foydalanuvchi topilmadi", HttpStatus.NOT_FOUND);
    }

    private void sendVerificationCode(String to, String otp) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        mimeMessage.setContent("<html><body><h1>Tasdiqlash kodi: </h1><h3>" + otp + "</h3></body></html>", "text/html");
        helper.setTo(to);
        helper.setFrom("nazarqosimovshukurullo093@gmail.com");
        helper.setSubject("Tasdiqlash kodi");

        mailSender.send(mimeMessage);
    }

    private static boolean isValidPassword(String pass) {
        String regExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,30}$";

        Pattern pattern = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(pass);

        return matcher.matches();
/*
    Kamida bitta raqam ([0-9]) bo'lishi kerak.
    Kamida bitta kichik harf ([a-z]) bo'lishi kerak.
    Kamida bitta katta harf ([A-Z]) bo'lishi kerak.
    Kamida bitta maxsus belgilar ([@#$%^&+=]) dan biri bo'lishi kerak.
    Hech qanday bo'sh joy (\s) bo'lmasligi kerak (\S+).
    Umumiy uzunlik kamida 8 va ko'pi bilan 30 ta belgi bo'lishi kerak.
*/
    }

    private static String generateOTP(){
        Random random = new Random();
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder otp = new StringBuilder();
        while (otp.length() < 6)
            otp.append(characters.charAt(random.nextInt(0, characters.length())));
        return otp.toString();
    }
}