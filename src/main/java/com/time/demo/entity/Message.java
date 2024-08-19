package com.time.demo.entity;

import com.time.demo.entity.enums.MessageType;
import com.time.demo.entity.templates.AbsMainEntity;
import com.time.demo.entity.templates.AbsUserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message")
public class Message extends AbsUserEntity {
    private String body;

    @Enumerated(value = EnumType.STRING)
    private MessageType type;

    private boolean isRead;

    private long originId;

    public Message(String body, MessageType type, boolean isRead) {
        this.body = body;
        this.type = type;
        this.isRead = isRead;
    }
}
