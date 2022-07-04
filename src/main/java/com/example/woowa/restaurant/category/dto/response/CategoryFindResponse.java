package com.example.woowa.restaurant.category.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryFindResponse {

    private final Long id;

    private final String name;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

}
