package com.time.demo.dto;

import com.time.demo.entity.enums.GroupCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private long id;
    private String name;
    private GroupCategory category;
    private int memberCount;
    private String createdDate;
}
