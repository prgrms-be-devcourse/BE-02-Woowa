package com.example.woowa.review.dto;

import com.example.woowa.customer.dto.CustomerDto;
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
