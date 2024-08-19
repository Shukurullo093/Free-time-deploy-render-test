package com.time.demo.config;

import com.time.demo.entity.Group;
import com.time.demo.entity.Users;
import com.time.demo.entity.enums.GroupCategory;
import com.time.demo.repository.GroupRepository;
import com.time.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.time.demo.entity.enums.Roles.*;

@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {

    @Autowired
    PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Value(value = "${spring.sql.init.mode}")
    String initMode;

    @Override
    public void run(ApplicationArguments args) {
        if (initMode.equals("always")) {
            userRepository.saveAll(Arrays.asList(
                    new Users(
                    "userA",
                    "userA",
                    "userA123",
                    "userA@gmail.com",
                    passwordEncoder.encode("P@ssw0rd"),
                    null,
                    null,
                    null,
                    null,
                    USER,
                    true
                ),
                    new Users(
                    "userB",
                    "userB",
                    "userB123",
                    "userB@gmail.com",
                    passwordEncoder.encode("P@ssw0rd"),
                    null,
                    null,
                    null,
                    null,
                    USER,
                    true)));
//            Group group=new Group();
//            group.setCategory(GroupCategory.WORK);
//            group.setName("130-20-group");
//            group.setCreatedBy(1L);
//            groupRepository.save(group);
        }
    }
}
