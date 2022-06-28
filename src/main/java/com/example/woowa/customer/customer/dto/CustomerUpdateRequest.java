package com.example.woowa.customer.customer.dto;

import javax.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdateRequest {
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "최소 8글자, 영어 대소문자와 숫자가 최소 1개씩 포함된 비밀번호가 아닙니다.")
    private String loginPassword;
}