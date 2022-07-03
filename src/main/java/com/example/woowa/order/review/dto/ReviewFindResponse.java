package com.example.woowa.order.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewFindResponse {
    private final Long id;
    private final String content;
    private final Integer scoreType;
}
