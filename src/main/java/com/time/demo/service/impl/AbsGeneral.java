package com.time.demo.service.impl;

import com.time.demo.dto.UserDto;
import com.time.demo.entity.Users;

import java.util.ArrayList;
import java.util.List;

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
}
