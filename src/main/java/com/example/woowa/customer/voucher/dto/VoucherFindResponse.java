package com.example.woowa.customer.voucher.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoucherFindResponse {
    private final Long id;
    private final String voucherType;
    private final String eventType;
    private final Integer discountValue;
    private final LocalDateTime expirationDate;
    private final String code;
}
