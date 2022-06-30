package com.example.woowa.restaurant.menugroup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuGroupResponse {

    private Long id;

    private String title;

    private String description;
}
