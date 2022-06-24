package com.example.woowa.customer.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerAddressDto {
    private Long id;
    private String address;
    private String nickname;
}
