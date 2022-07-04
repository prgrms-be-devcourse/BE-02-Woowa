package com.example.woowa.customer.customer.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class CustomerAddressUpdateRequest {
    @NotBlank
    private final String defaultAddress;
    @NotBlank
    @Length(max = 100)
    private final String detailAddress;
    @NotBlank
    @Length(max = 10)
    private final String nickname;
}