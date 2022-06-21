package com.example.woowa.voucher.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateVoucherDto {
  private String voucherType;
  private Integer discountValue;
  private LocalDateTime expirationDate;
  private String code;
}
