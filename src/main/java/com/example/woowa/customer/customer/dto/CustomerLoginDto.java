package com.example.woowa.customer.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerLoginDto {
    private String loginId;
    private String loginPassword;
}