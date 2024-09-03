package com.time.demo.service.impl;

import com.time.demo.dto.ContactsDto;
import com.time.demo.dto.GroupDto;
import com.time.demo.dto.ProfileDto;
import com.time.demo.entity.Contacts;
import com.time.demo.entity.Group;
import com.time.demo.entity.Users;
import com.time.demo.entity.enums.ContactType;
import com.time.demo.repository.ContactsRepository;
import com.time.demo.repository.GroupRepository;
import com.time.demo.repository.UserRepository;
import com.time.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends AbsGeneral implements UserService {
    private final UserRepository userRepository;
    private final ContactsRepository contactsRepository;
    private final GroupRepository groupRepository;

    @Override
    public Map<String, List<ContactsDto>> getContactsList(Users user) {
        Map<String, List<ContactsDto>> map = new HashMap<>();
        List<Contacts> allByCreatedBy = contactsRepository.findAllByCreatedByOrContactIdOrderByCreatedAtDesc(user.getId(), user.getId());
        List<ContactsDto> contactsDtoList = new ArrayList<>();
        for (Contacts contacts : allByCreatedBy) {
            Users contact;
            boolean isSender = false, isSave=false;
            String groupName = "";
            if (contacts.getCreatedBy().equals(user.getId())) {
                Optional<Users> usersOptional = userRepository.findById(contacts.getContactId());
                if (usersOptional.isPresent()) {
                    contact = usersOptional.get();
                    isSender = true;
                    isSave=contacts.getContactType1() != ContactType.ONLY_GROUP;
                    groupName = contacts.getGroupId1() != 0 ? groupRepository.findById(contacts.getGroupId1()).get().getName() : null;
                } else contact = new Users();
            } else {
                contact = userRepository.findById(contacts.getCreatedBy()).get();
                groupName = contacts.getGroupId2() != 0 ? groupRepository.findById(contacts.getGroupId2()).get().getName() : null;
                isSave=contacts.getContactType2() != ContactType.ONLY_GROUP;
            }

            String avatarLink = contact.getImage() != null ? "localhost:8080/user/avatar/" + contact.getImage().getHashId() : null;

            contactsDtoList.add(new ContactsDto(
                    contact.getId(),
                    contact.getFirstName(),
                    contact.getLastName(),
                    contact.getUsername1(),
                    groupName,
                    avatarLink,
                    getFormattedDate(contacts.getCreatedAt(), "dd/MM/yyyy hh:mm"),
                    isSave,
                    isSender));
        }
        map.put("contactsList", contactsDtoList);
        return map;
    }

    @Override
    public Map<String, ProfileDto> getProfileInfo(Users user) {
        Map<String, ProfileDto> map = new HashMap<>();
        Optional<Users> usersOptional = userRepository.findById(user.getId());
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            ProfileDto profileDto = new ProfileDto(
                    users.getFirstName(),
                    users.getLastName(),
                    users.getUsername1(),
                    users.getEmail(),
                    users.getPhone()
            );
            map.put("user", profileDto);
        }
        return map;
    }

    @Override
    public Map<String, List<GroupDto>> getGroupsList(Users user) {
        Map<String, List<GroupDto>> map = new HashMap<>();
        List<Group> groupList = groupRepository.findAllByCreatedBy(user.getId());
        List<GroupDto> groupDtos = new ArrayList<>();
        for (Group group : groupList) {
            groupDtos.add(new GroupDto(
                    group.getId(),
                    group.getName(),
                    group.getCategory(),
                    contactsRepository.countAllByGroupId1OrGroupId2(group.getId(), group.getId()),
                    getFormattedDate(group.getCreatedAt(), "dd/MM/yyyy")
            ));
        }
        map.put("groups", groupDtos);
        return map;
    }
}

