package com.example.woowa.customer.dto;

import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCustomerDto {
  @Pattern(regexp = "/^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/gm", message ="최소 8글자, 영어 대소문자와 숫자가 포함된 비밀번호여야 합니다.")
  private String loginPassword;
}