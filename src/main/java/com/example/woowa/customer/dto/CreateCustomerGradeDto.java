package com.example.woowa.customer.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCustomerGradeDto {
  @Positive
  private Integer orderCount;
  @NotBlank
  private String grade;
  @Positive
  private Integer discountPrice;
  @Positive
  private Integer voucherCount;
}
