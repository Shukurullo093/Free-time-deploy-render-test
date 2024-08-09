package com.time.demo.service;

import com.time.demo.dto.ContactsDto;
import com.time.demo.entity.Users;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface UserService {
    Map<String, List<ContactsDto>> getContactsList(Users user);
}
