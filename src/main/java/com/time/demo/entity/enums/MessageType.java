package com.time.demo.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MessageType {
    INVITATION("invitation"),
    NOTIFICATION("notification"),
    CONTACT_DELETE("contact delete");

    @Getter
    public final String description;
}
