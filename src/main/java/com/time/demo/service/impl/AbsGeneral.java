package com.time.demo.service.impl;

import com.time.demo.dto.UserDto;
import com.time.demo.entity.Users;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbsGeneral {
    public UserDto getUserDtoFromUser(Users user, String jwtToken) {
            String avatarLink = user.getImage() != null ? "localhost:8080/user/avatar/" + user.getImage().getHashId() : null;
            return new UserDto(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getUsername1(),
                    user.getEmail(),
                    avatarLink,
                    jwtToken
            );
    }

    public List<UserDto> getUserDtoListFromUsersList(List<Users> users) {
        List<UserDto> userDtoList = new ArrayList<>();
        users.forEach(users1 -> {
            String avatarLink = users1.getImage() != null ? "localhost:8080/user/avatar/" + users1.getImage().getHashId() : null;
            userDtoList.add(
                new UserDto(
                        users1.getFirstName(),
                        users1.getLastName(),
                        users1.getUsername1(),
                        users1.getEmail(),
                        avatarLink,
                        null
                ));
        });
        return userDtoList;
    }

    public boolean isValidUserName(String username) {
        // [48-57] ->numbers | [97-122] -> lowercases
        for (int i = 0; i < username.length(); i++) {
            int asciiIndex = username.charAt(i);
            if ((asciiIndex < 48 || (asciiIndex > 57 && asciiIndex < 97) || asciiIndex > 122))
                return true;
        }
        return false;
    }

    public boolean isValidPassword(String pass) {
        String regExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$&*+=])(?=\\S+$).{8,30}$";

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

    public boolean isValidPhoneNumber(String phoneNumber){
        return phoneNumber.length() == 13 && phoneNumber.startsWith("+998");
    }

    public String getFormattedDate(Timestamp dateTime, String format){
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(dateTime);
    }
}
