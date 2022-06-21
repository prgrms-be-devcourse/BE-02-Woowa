package com.example.woowa.customer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCustomerGradeDto {
  private Integer orderCount;
  private String grade;
  private Integer discountPrice;
  private Integer voucherCount;
}