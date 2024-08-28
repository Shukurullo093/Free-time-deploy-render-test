package com.time.demo.entity;

import com.time.demo.entity.enums.ContactType;
import com.time.demo.entity.templates.AbsUserEntity;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "contacts")
public class Contacts extends AbsUserEntity {
    @ManyToOne
    private Users contact;

    @ManyToOne
//    @JoinColumn(name = "group_id")
    private Group group;

    @Enumerated(value = EnumType.STRING)
    private ContactType contactType;

//    @ManyToOne
////    @JoinColumn(name = "group_id")
//    private Group group2;
//
//    @Enumerated(value = EnumType.STRING)
//    private ContactType contactType2;
}
