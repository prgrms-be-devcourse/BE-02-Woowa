package com.example.woowa.customer.dto;

import javax.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCustomerAddressDto {
  private String address;
  private String nickname;
  private @Valid CustomerDto customer;
}
