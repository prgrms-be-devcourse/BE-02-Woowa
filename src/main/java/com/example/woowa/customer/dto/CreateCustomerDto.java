package com.example.woowa.customer.dto;

import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCustomerDto {
  @Pattern(regexp = "/^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-z0-9]{5,10}$/", message = "최소 5글자에서 10글자, 특수문자를 제외한 영숫자가 포함된 아이디가 아닙니다.")
  private String loginId;
  @Pattern(regexp = "/^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$/", message ="최소 8글자, 영어 대소문자와 숫자가 최소 1개씩 포함된 비밀번호가 아닙니다.")
  private String loginPassword;
  @Pattern(regexp = "/^(19|20)\\d{2}[-](0?[1-9]|1[0-2])[-](0?[1-9]|[12]\\d|3[01])$/", message = "YYYY-MM-dd 형태로 작성해야 합니다.")
  private String birthdate;
}