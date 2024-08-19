package com.time.demo.entity;

import com.time.demo.entity.enums.ContactType;
import com.time.demo.entity.templates.AbsUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "contacts")
public class Contacts extends AbsUserEntity {
    @ManyToOne
    private Users contact;

    @ManyToOne
    private Group group;

    @Enumerated(value = EnumType.STRING)
    private ContactType contactType;
}
