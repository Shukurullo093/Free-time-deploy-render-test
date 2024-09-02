package com.time.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGroupDto {
    @NotBlank(message = "The Name is required.")
    private String name;

    @NotBlank(message = "The Category is required.")
    private String category;
}
