package com.example.woowa.voucher.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateVoucherDto {
    private String voucherType;
    private Integer discountValue;
    private LocalDateTime expirationDate;
    private String code;
}
