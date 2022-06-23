package com.example.woowa.customer.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCustomerAddressDto {
  @NotBlank
  private String address;
  @NotBlank
  private String nickname;
}
