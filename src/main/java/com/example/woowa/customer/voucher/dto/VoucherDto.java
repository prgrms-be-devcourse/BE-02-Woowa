package com.example.woowa.customer.voucher.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoucherDto {
    private Long id;
    private String voucherType;
    private Integer discountValue;
    private LocalDateTime expirationDate;
    private String code;
}
