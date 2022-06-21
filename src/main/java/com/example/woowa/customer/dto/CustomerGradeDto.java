package com.example.woowa.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerGradeDto {
  private Integer orderCount;
  private String grade;
  private Integer discountPrice;
  private Integer voucherCount;
}
