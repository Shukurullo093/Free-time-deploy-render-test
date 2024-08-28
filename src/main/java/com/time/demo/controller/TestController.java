package com.time.demo.controller;

import com.time.demo.entity.*;
import com.time.demo.repository.*;
import com.time.demo.service.impl.AbsGeneral;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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
    private final GroupRepository groupRepository;
    private final MessageRepository messageRepository;

    @GetMapping()
    public @ResponseBody String test() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Users> usersList = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        stringBuilder.append("<h2><b>Users List: </b></h2><h4>the password of all users is P@ssw0rd</h4>");
        stringBuilder.append("<table style='border-collapse: collapse;'><tr>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>NAME</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>SURNAME</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>USERNAME</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>EMAIL</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>PHONE</th>" +
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
                    .append(usersList.get(j).getPhone()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).getImage() != null ? usersList.get(j).getImage().getId() : null).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).isEnabled()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(usersList.get(j).getEmailCode()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(getFormattedDate(usersList.get(j).getCreatedAt(), "dd/MM/yyyy HH:mm")).append("</td>");
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

        List<Group> groupList = groupRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        stringBuilder.append("<h2><b>Group List:</b></h2>");
        stringBuilder.append("<table style='border-collapse: collapse;'><tr>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CREATED-BY</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>NAME</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CATEGORY</th></tr>");
        for (int j = 0; j < groupList.size(); j++) {
            stringBuilder
                    .append("<tr><td style='border: 1px dashed black; padding: 1px 3px;'>")
                    .append(groupList.get(j).getId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(groupList.get(j).getCreatedBy()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(groupList.get(j).getName()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(groupList.get(j).getCategory()).append("</td>");
        }
        stringBuilder.append("</tr></table>");

        List<Contacts> contactsList = contactsRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        stringBuilder.append("<h2><b>Contacts List:</b></h2>");
        stringBuilder.append("<table style='border-collapse: collapse;'><tr>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CREATED-BY</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CONTACT-ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>GROUP 1</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CONTACT-TYPE 1</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>GROUP 2</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CONTACT-TYPE 2</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CREATED-AT</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>UPDATED-AT</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>UPDATED-BY</th></tr>");
        for (int j = 0; j < contactsList.size(); j++) {
            stringBuilder.append("<tr><td style='border: 1px dashed black; padding: 1px 3px;'>")
                    .append(contactsList.get(j).getId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getCreatedBy()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getContact().getId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getGroup().getId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getContactType()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getGroup().getId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getContactType()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(getFormattedDate(contactsList.get(j).getCreatedAt(), "dd/MM/yyyy HH:mm")).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(getFormattedDate(contactsList.get(j).getUpdatedAt(), "dd/MM/yyyy HH:mm")).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(contactsList.get(j).getUpdatedBy()).append("</td>");
        }
        stringBuilder.append("</tr></table>");

        List<Message> messageList = messageRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        stringBuilder.append("<h2><b>Message List:</b></h2>");
        stringBuilder.append("<table style='border-collapse: collapse;'><tr>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CREATED-BY</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>BODY</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>TYPE</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>ORIGIN ID</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>IS READ</th>" +
                "<th style='border: 2px solid black; font-weight: bold; padding: 5px;'>CREATED AT</th></tr>");
        for (int j = 0; j < messageList.size(); j++) {
            stringBuilder.append("<tr><td style='border: 1px dashed black; padding: 1px 3px;'>")
                    .append(messageList.get(j).getId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(messageList.get(j).getCreatedBy()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(messageList.get(j).getBody()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(messageList.get(j).getType()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(messageList.get(j).getOriginId()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(messageList.get(j).isRead()).append("</td><td style='border: 1px dashed black; padding: 1px 3px; '>")
                    .append(getFormattedDate(messageList.get(j).getCreatedAt(), "dd/MM/yyyy HH:mm")).append("</td>");
        }
        stringBuilder.append("</tr></table>");

        return "<h1>Project is working...</h1>" + stringBuilder;
    }
}
