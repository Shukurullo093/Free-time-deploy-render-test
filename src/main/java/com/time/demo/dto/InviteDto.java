package com.time.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteDto {
    @NotBlank(message = "The username is required.")
    private String username;

    @NotBlank(message = "The message is required.")
    private String message;
}
