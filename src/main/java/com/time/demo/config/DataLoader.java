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
        List<Users> usersList=new ArrayList<>(Arrays.asList(
                new Users("userA", "userA", "userA123", "userA@gmail.com", passwordEncoder.encode("P@ssw0rd"), null, null, null, null, USER, true),
                new Users("userB", "userB", "userB123", "userB@gmail.com", passwordEncoder.encode("P@ssw0rd"), null, null, null, null, USER, true),
                new Users("userC", "userC", "userC123", "userC@gmail.com", passwordEncoder.encode("P@ssw0rd"), null, null, null, null, USER, true),
                new Users("userD", "userD", "userD123", "userD@gmail.com", passwordEncoder.encode("P@ssw0rd"), null, null, null, null, USER, true),
                new Users("userE", "userE", "userE123", "userE@gmail.com", passwordEncoder.encode("P@ssw0rd"), null, null, null, null, USER, true),
                new Users("userF", "userF", "userF123", "userF@gmail.com", passwordEncoder.encode("P@ssw0rd"), null, null, null, null, USER, true),
                new Users("userG", "userG", "userG123", "userG@gmail.com", passwordEncoder.encode("P@ssw0rd"), null, null, null, null, USER, true),
                new Users("userH", "userH", "userH123", "userH@gmail.com", passwordEncoder.encode("P@ssw0rd"), null, null, null, null, USER, true)
        ));

        if (initMode.equals("always") && userRepository.findAll().isEmpty()) {
            userRepository.saveAll(usersList);
        }
    }
}
