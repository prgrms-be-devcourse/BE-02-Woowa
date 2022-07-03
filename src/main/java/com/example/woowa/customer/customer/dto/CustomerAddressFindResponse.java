package com.example.woowa.customer.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerAddressFindResponse {
    private final Long id;
    private final String address;
    private final String nickname;
}
