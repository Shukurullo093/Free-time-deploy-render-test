package com.time.demo.entity;

import com.time.demo.entity.templates.AbsMainLongEntity;
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
@Table(name = "images")
public class UserImage extends AbsMainLongEntity{
    private String name;

    private String extension;

    private Long fileSize;

    private String hashId;

    private String contentType;

    private byte[] imageByte;

//    @OneToOne(mappedBy = "image")
//    @EqualsAndHashCode.Exclude
//    private Users users;
}
