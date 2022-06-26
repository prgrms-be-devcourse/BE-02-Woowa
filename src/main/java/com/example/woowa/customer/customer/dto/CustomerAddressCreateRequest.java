package com.example.woowa.customer.customer.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerAddressCreateRequest {
    @NotBlank
    private String defaultAddress;
    @NotBlank
    private String detailAddress;
    @NotBlank
    private String nickname;
}
