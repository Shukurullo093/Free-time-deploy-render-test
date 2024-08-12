package com.time.demo.controller;

import com.time.demo.entity.Contacts;
import com.time.demo.entity.UserImage;
import com.time.demo.entity.Users;
import com.time.demo.repository.ContactsRepository;
import com.time.demo.repository.UserImageRepository;
import com.time.demo.repository.UserRepository;
import com.time.demo.service.impl.AbsGeneral;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController extends AbsGeneral {
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final ContactsRepository contactsRepository;

    @GetMapping(value = "", produces = MediaType.TEXT_HTML_VALUE)
    public @ResponseBody String test() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Users> usersList = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        stringBuilder.append("<h2><b>Users List: </b></h2>");
        stringBuilder.append("<table style='border-collapse: collapse;'><tr>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>NAME</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>SURNAME</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>USERNAME</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>EMAIL</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>IMAGE</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>ENABLED</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>EMAIL-CODE</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CREATED-AT</th></tr>");
        for (int j = 0; j < usersList.size(); j++) {
            stringBuilder.append("<tr><td style='border: 1px dashed black; padding: 1px 3px;'>")
                    .append(usersList.get(j).getId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).getFirstName()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).getLastName()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).getUsername1()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).getEmail()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).getImage() != null ? usersList.get(j).getImage().getId() : null).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).isEnabled()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).getEmailCode()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).getCreatedAt()).append("</td>");
        }
        stringBuilder.append("</tr></table>");

        List<UserImage> imageList = userImageRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        stringBuilder.append("<h2><b>Users Image List:</b></h2>");
        stringBuilder.append("<table style='border-collapse: collapse;'><tr>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>NAME</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>HASH-ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>EXTENSION</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>FILE SIZE</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>IMAGE</th></tr>");
        for (int j = 0; j < imageList.size(); j++) {
            stringBuilder.append("<tr><td style='border: 1px dashed black; padding: 1px 3px;'>")
                    .append(imageList.get(j).getId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(imageList.get(j).getName()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(imageList.get(j).getHashId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(imageList.get(j).getExtension()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(imageList.get(j).getFileSize()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '><img src=\"http://localhost:8080/user/avatar/")
                    .append(imageList.get(j).getHashId()).append("\" width='50' ></td>");
        }
        stringBuilder.append("</tr></table>");

        List<Contacts> contactsList = contactsRepository.findAll();
        stringBuilder.append("<h2><b>Contacts List:</b></h2>");
        stringBuilder.append("<table style='border-collapse: collapse;'><tr>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CREATED-BY</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>FRIEND-ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>STATUS</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CREATED-AT</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>UPDATED-AT</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>UPDATED-BY</th></tr>");
        for (int j = 0; j < contactsList.size(); j++) {
            stringBuilder.append("<tr><td style='border: 1px dashed black; padding: 1px 3px;'>")
                    .append(contactsList.get(j).getId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getCreatedBy()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getContact().getId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getStatus()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getCreatedAt()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getUpdatedAt()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getUpdatedBy()).append("</td>");
        }
        stringBuilder.append("</tr></table>");

        return "<h1>Project is working...</h1>" + stringBuilder;
    }
}
