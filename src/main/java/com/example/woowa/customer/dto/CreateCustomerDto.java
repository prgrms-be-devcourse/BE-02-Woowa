package com.example.woowa.customer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCustomerDto {
  private String loginId;
  private String loginPassword;
  private String birthdate;
}
