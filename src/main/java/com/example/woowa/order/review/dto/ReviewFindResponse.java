package com.example.woowa.order.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewFindResponse {
    private Long id;
    private String content;
    private Integer scoreType;
}
