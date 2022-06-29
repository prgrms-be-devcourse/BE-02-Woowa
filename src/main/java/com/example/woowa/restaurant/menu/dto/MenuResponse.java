package com.example.woowa.restaurant.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuResponse {

    Long id;

    String title;

    String description;

    Integer price;
}
