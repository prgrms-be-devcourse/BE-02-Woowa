package com.example.woowa.customer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateCustomerDto {
  private String loginId;
  private String loginPassword;
}