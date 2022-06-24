package com.example.woowa.order.review.dto;

import com.example.woowa.customer.customer.dto.CustomerDto;

import javax.validation.Valid;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateReviewDto {
    private String content;
    private String scoreType;
    private @Valid CustomerDto customer;
}
