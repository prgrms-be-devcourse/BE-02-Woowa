package com.example.woowa.restaurant.category.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryCreateResponse {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

}
