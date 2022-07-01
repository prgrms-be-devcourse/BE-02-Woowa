package com.example.woowa.restaurant.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuResponse {

    private final Long id;

    private final String title;

    private final String description;

    private Integer price;
}
