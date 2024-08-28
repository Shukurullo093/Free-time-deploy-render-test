package com.time.demo.service.impl;

import com.time.demo.dto.*;
import com.time.demo.entity.*;
import com.time.demo.entity.enums.ContactType;
import com.time.demo.entity.enums.GroupCategory;
import com.time.demo.entity.enums.MessageType;
import com.time.demo.repository.*;
import com.time.demo.service.UserRestService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserRestServiceImpl extends AbsGeneral implements UserRestService {
    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;
    private final ContactsRepository contactsRepository;
    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;

    @Autowired
    JavaMailSender mailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Value("${spring.mail.username}")
    private String host;

    @Override
    public ApiResponse uploadImage(MultipartFile image, String email) throws IOException {
        if (!Objects.requireNonNull(image.getContentType()).split("/")[0].equals("image")) // only images, image/*
            return new ApiResponse("File formati xato", 400);
        Optional<Users> usersOptional = userRepository.findByEmail(email);
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            UserImage usersImage;
            if (users.getImage() != null) {
                Optional<UserImage> imageOptional = userImageRepository.findById(users.getImage().getId());
                usersImage = imageOptional.orElseGet(UserImage::new);
            } else usersImage = new UserImage();

            usersImage.setName(image.getOriginalFilename());
            usersImage.setExtension(getExtension(image.getOriginalFilename()));
            String encodedString = Base64.getEncoder().encodeToString(Objects.requireNonNull(image.getOriginalFilename()).getBytes());
            usersImage.setHashId(encodedString.substring(0, encodedString.length() - 2));
            usersImage.setFileSize(image.getSize());
            usersImage.setContentType(image.getContentType());
            usersImage.setImageByte(image.getBytes());
            UserImage savedImage = userImageRepository.save(usersImage);

            users.setImage(savedImage);
            userRepository.save(users);
        }
        return new ApiResponse("Profil uchun rasm yuklandi", 200);
    }

    @Transactional
    @Override
    public ApiResponse sendJoinRequest(Users user, String username, String body) {
        if (contactsRepository.existsByContactUsername1AndCreatedBy(username, user.getId()))
            return new ApiResponse("Ushbu foydalanuvchi bilan allaqachon bog'langansiz", 200);

        Optional<Users> byUsername = userRepository.findByUsername1(username);
        if (byUsername.isPresent() && byUsername.get().isEnabled()) {
            Message message = new Message(
                    body,
                    MessageType.INVITATION,
                    false
            );
            messageRepository.save(message);
            return new ApiResponse("Taklif havolasi yuborildi", 200);
        }
        return new ApiResponse("Bunday foydalanuvchi nom egasi topilmadi", 400);
    }

    @Override
    public ApiResponse sendInvitationLetterToEmail(String email, Users user) throws MessagingException {
        String htmlMsg = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <style>\n" +
                "        body{\n" +
                "            width: 100%;\n" +
                "        }\n" +
                "        body,\n" +
                "        .container{\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            align-items: center;\n" +
                "            justify-content: center;\n" +
                "            font-family: 'Times New Roman', Times, serif;\n" +
                "            text-align: justify;\n" +
                "        }\n" +
                "        .accept{\n" +
                "            background-color: #eaf2ed; \n" +
                "            margin: 5px 0 0;\n" +
                "            padding: 1rem;\n" +
                "            border-top: 1px solid blueviolet;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "        }\n" +
                "        a{\n" +
                "            text-decoration: none;\n" +
                "            background-color: green;\n" +
                "            padding: 8px 30px;\n" +
                "            border-radius: 13px;\n" +
                "            font-size: 20px;\n" +
                "            letter-spacing: 2px;\n" +
                "            color: white;\n" +
                "        }\n" +
                "        .footer{\n" +
                "            background-color: black;\n" +
                "            text-align: justify;\n" +
                "            color: white;\n" +
                "            padding: 10px;\n" +
                "            font-size: 16px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\" style=\"width: 550px; border-radius: 5px;\">\n" +
                "        <img src=\"https://static.vecteezy.com/system/resources/previews/021/480/975/original/you-re-invited-calligraphy-text-with-elegant-golden-frame-hand-drawn-style-lettering-design-for-greeting-cards-and-invitations-vector.jpg\" width=\"540\" height=\"250\" style=\"margin-top: 5px;\">\n" +
                "        <div class=\"user\" style=\"background-color: aliceblue; padding: 10px;\">\n" +
                "            <h2 style=\"border-top: 1px solid blueviolet; padding-top: 1rem; margin: 0;\">Assalomu alaykum</h2><br>\n" +
                "            <span style=\"font-size: 18px; letter-spacing: 1px; line-height: 25px;\">Siz <u><strong>free.time.uz</strong></u> veb-sahifasiga <b>{firstname} {lastname}</b> tomonidan taklif qilindingiz. </span>\n" +
                "        </div>\n" +
                "        <div class=\"site-info\" style=\"font-size: 18px;\">\n" +
                "            <h3 style=\"margin: 5px 10px;\"><b>Veb-saytga a'zo bo'lish orqali quyidagi imkoniyatlarga ega bo'lasiz:</b></h3>\n" +
                "            <ul style=\"line-height: 25px; margin: 0 10px;\">\n" +
                "                <li>Kunlik vazifalarni rejalashtirish;</li>\n" +
                "                <li>Hodisalar haqida qulay tarzda telegram bot va email orqali bildirishnoma olish;</li>\n" +
                "                <li>Do'stlar va biznes hamkorlar bilan qulay vaqtda uchrashuv belgilash;</li>\n" +
                "            </ul>\n" +
                "            <div class=\"accept\">\n" +
                "                <a href=\"javascript:void(0);\">Taklifni qabul qilish</a>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"footer\">\n" +
                "            <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Rerum quaerat praesentium assumenda inventore, incidunt quod? Nulla, ipsum.</p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
        mimeMessage.setContent(htmlMsg, "text/html");
        helper.setTo(email);
        helper.setFrom(host);
        helper.setSubject("Taklif havolasi");
        mailSender.send(mimeMessage);
        return new ApiResponse("Emailga taklif havolasi yuborildi", 200);
    }

    @Override
    public List<UserDto> getUsersByUsername(String username, Users user) {
        Pageable pageable = PageRequest.of(0, 5);
        if (username != null) {
            List<Users> allByUsernameWithQuery = userRepository.findAllByUsernameWithQuery(username, user.getUsername1(), pageable);
            return getUserDtoListFromUsersList(allByUsernameWithQuery);
        }
        return null;
    }

    @Override
    public ApiResponse addUserToContactOrGroup(Long userId, Long groupId, boolean save, Users user) {
        if (!groupRepository.existsByIdAndCreatedBy(groupId, user.getId()))
            return new ApiResponse("Sizda guruhga foydalanuvchi qo'shishga ruxsat yo'q", 403);
        Optional<Users> usersOptional = userRepository.findById(userId);
        if (usersOptional.isEmpty()) {
            return new ApiResponse("Fodalanuvchi topilmadi", 404);
        }
        Contacts contact = new Contacts();
        contact.setContact(usersOptional.get());
        saveContact(groupId, save, contact);
        return new ApiResponse("Contact saqlandi", 200);
    }

    @Override
    public UserDto getProfileInfo(Users user) {
        Optional<Users> usersOptional = userRepository.findByEmail(user.getEmail());
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            return getUserDtoFromUser(users, null);
        }
        return new UserDto();
    }

    @Override
    public ApiResponse deleteInvitation(Long id, Users user) {
        Optional<Contacts> contactsOptional = contactsRepository.findById(id);
        if (contactsOptional.isPresent()) {
            Contacts contacts = contactsOptional.get();
            if (contacts.getCreatedBy().equals(user.getId())) {
                contactsRepository.deleteById(id);
                return new ApiResponse("Foydalanuvchi bilan aloqa uzildi", 200);
            }
        }
        return new ApiResponse("Taklif havolasi topilmadi", 400);
    }

    @Override
    public ApiResponse updateProfile(ProfileDto profileDto, Users user) {
        if (isValidUserName(profileDto.getUsername()))
            return new ApiResponse("Yaroqsiz foydalanuvchi nomi", 400);
        if (userRepository.existsByUsername1AndEmailNot(profileDto.getUsername(), user.getEmail()))
            return new ApiResponse("Bu foydalanuvchi nomi band", 400);
        if (userRepository.existsByPhone(profileDto.getPhone()) && profileDto.getPhone() != null && profileDto.getPhone().isEmpty())
            return new ApiResponse("Bu telefon raqam boshqa hisob uchun ro'yhatdan o'tgan", 400);
        if ((profileDto.getFirstName() == null || profileDto.getFirstName().isEmpty()) &&
                (profileDto.getLastName() == null || profileDto.getLastName().isEmpty()) &&
                (profileDto.getUsername() == null || profileDto.getUsername().isEmpty()))
            return new ApiResponse("Barcha maydonlar to'ldirilishi shart", 400);
        if (profileDto.getPhone() != null && profileDto.getPhone().isEmpty())
            if (isValidPhoneNumber(Objects.requireNonNull(profileDto.getPhone())))
                return new ApiResponse("Yaroqsiz telefon raqami", 400);
        if (user.getEmail().equals(profileDto.getEmail())) {
            Optional<Users> usersOptional = userRepository.findById(user.getId());
            if (usersOptional.isPresent()) {
                Users users = usersOptional.get();
                users.setFirstName(profileDto.getFirstName());
                users.setLastName(profileDto.getLastName());
                users.setUsername1(profileDto.getUsername());
                users.setPhone(profileDto.getPhone());
                userRepository.save(users);
                return new ApiResponse("Ma'lumotlar muvaffaqqiyatli yangilandi", 200);
            }
        }
        return new ApiResponse("Sizda ma'lumotlarni o'zgartirishga ruxsat yo'q", 403);
    }

    @Override
    public ApiResponse updatePassword(PasswordDto passwordDto, Users user) {
        if (!isValidPassword(passwordDto.getNewPassword()))
            return new ApiResponse("Yaroqsiz parol", 400);
        if ((passwordDto.getOldPassword() == null || passwordDto.getOldPassword().isEmpty()) &&
                (passwordDto.getNewPassword() == null || passwordDto.getNewPassword().isEmpty()))
            return new ApiResponse("Maydonlar to'ldirilishi zarur", 400);
        Optional<Users> usersOptional = userRepository.findById(user.getId());
        if (usersOptional.isPresent()) {
            Users users = usersOptional.get();
            if (passwordEncoder.matches(passwordDto.getOldPassword(), users.getPassword())) {
                users.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
                userRepository.save(users);
                return new ApiResponse("Parol o'zgartirildi", 200);
            }
        }
        return new ApiResponse("Foydalanuvchi ma'lumotlari topilmadi", 403);
    }

    @Override
    public ApiResponse createGroup(Users user, String name, String category) {
        if (groupRepository.existsByNameAndCreatedBy(name, user.getId()))
            return new ApiResponse("Bunday guruh nomini allaqachon ishlatgansiz", 409);

        Group group = new Group(
                name,
                GroupCategory.valueOf(category)
        );
        groupRepository.save(group);
        return new ApiResponse("Guruh yaratildi", 200);
    }

    @Override
    public ApiResponse deleteGroup(Users user, long groupId) {
        //  must check to exist if have any contact linked to group before delete group
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (groupOptional.isPresent()) {
            if (groupOptional.get().getCreatedBy().equals(user.getId())) {
                //  delete all contacts related to group from contacts table
                groupRepository.deleteById(groupId);
                return new ApiResponse("Guruh o'chirildi", 200);
            }
            return new ApiResponse("Sizda guruhni o'chirishga vakolat yo'q", 403);
        }
        return new ApiResponse("Guruh ma'lumotlari topilmadi", 404);
    }

    @Override
    public ApiResponse updateUserContact(Long contactId, Long groupId, boolean save, Users user) {
        Optional<Contacts> contactsOptional = contactsRepository.findById(contactId);
        if (contactsOptional.isPresent()) {
            Contacts contact = contactsOptional.get();
            if (contact.getCreatedBy().equals(user.getId())) {
                saveContact(groupId, save, contact);
                return new ApiResponse("Contact ma'lumotlari yangilandi", 200);
            }
            return new ApiResponse("Contactni yangilashga ruxsat yo'q", 403);
        }
        return new ApiResponse("Contact ma'lumotlari topilmadi", 404);
    }

    private void saveContact(Long groupId, boolean save, Contacts contact) {
        Optional<Group> groupOptional = groupRepository.findById(groupId);
        if (save && groupId > 0) {
            contact.setGroup(groupOptional.orElse(null));
            contact.setContactType(ContactType.GROUP_AND_CONTACT);
        } else if (save && groupId == 0) {
            contact.setContactType(ContactType.ONLY_CONTACT);
        } else {
            contact.setGroup(groupOptional.orElse(null));
            contact.setContactType(ContactType.ONLY_GROUP);
        }
        contactsRepository.save(contact);
    }

    private String getExtension(String fileName) {
        String ext = null;
        if (fileName != null && !fileName.isEmpty()) {
            int dot = fileName.lastIndexOf(".");
            if (dot > 0 && dot <= fileName.length() - 2) {
                ext = fileName.substring(dot + 1);
            }
        }
        return ext;
    }
}
