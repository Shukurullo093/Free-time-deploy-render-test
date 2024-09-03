package com.time.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptInvitationDto {
    @NotBlank(message = "The username id is required")
    private String username;

    @NotBlank(message = "The group id is required")
    private long groupId;

    @NotBlank(message = "The isSave is required")
    private boolean isSave;

//    @NotBlank(message = "The isOwner id is required")
//    private boolean isOwner;
}
