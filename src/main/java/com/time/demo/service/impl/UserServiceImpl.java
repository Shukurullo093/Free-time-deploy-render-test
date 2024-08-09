package com.time.demo.service.impl;

import com.time.demo.dto.ContactsDto;
import com.time.demo.entity.Contacts;
import com.time.demo.entity.Users;
import com.time.demo.repository.ContactsRepository;
import com.time.demo.repository.UserRepository;
import com.time.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ContactsRepository contactsRepository;

    @Override
    public Map<String, List<ContactsDto>> getContactsList(Users user) {
        Map<String, List<ContactsDto>> map=new HashMap<>();
        List<Contacts> allByCreatedBy = contactsRepository.findAllByCreatedBy(user.getId());
        List<ContactsDto> contactsDtoList = new ArrayList<>();
        for (Contacts contacts : allByCreatedBy) {
            Users contact = contacts.getContact();
            String avatarLink = user.getImage() != null ? "localhost:8080/user/avatar/" + contact.getImage().getHashId() : null;
            contactsDtoList.add(new ContactsDto(
                    contact.getFirstName(),
                    contact.getLastName(),
                    contact.getUsername(),
                    contact.getEmail(),
                    avatarLink,
                    contacts.getStatus()));
        }
        map.put("contactsList", contactsDtoList);
        return map;
    }
}
