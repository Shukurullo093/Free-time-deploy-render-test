package com.time.demo.entity;

import com.time.demo.entity.enums.GroupCategory;
import com.time.demo.entity.templates.AbsUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "_group")
public class Group extends AbsUserEntity {
    @Column(nullable = false, length = 30)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private GroupCategory category;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
//    private List<Contacts> contacts;

//    public Group(String name, GroupCategory groupCategory) {
//        this.name=name;
//        this.category=groupCategory;
//    }
}
