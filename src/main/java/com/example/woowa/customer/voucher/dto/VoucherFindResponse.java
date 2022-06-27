package com.example.woowa.customer.voucher.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoucherFindResponse {
    private Long id;
    private String voucherType;
    private String eventType;
    private Integer discountValue;
    private LocalDateTime expirationDate;
    private String code;
}
