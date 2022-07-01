package com.example.woowa.restaurant.menugroup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuGroupResponse {

    private final Long id;

    private final String title;

    private final String description;
}
