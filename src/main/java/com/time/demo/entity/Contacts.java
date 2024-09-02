package com.time.demo.entity;

import com.time.demo.entity.enums.ContactType;
import com.time.demo.entity.templates.AbsUserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "contacts")
@AllArgsConstructor
@NoArgsConstructor
public class Contacts extends AbsUserEntity {
    private long contactId;

    private long groupId1;

    @Enumerated(value = EnumType.STRING)
    private ContactType contactType1;

    private long groupId2;

    @Enumerated(value = EnumType.STRING)
    private ContactType contactType2;
}

