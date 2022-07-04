package com.example.woowa.customer.voucher.converter;

import com.example.woowa.customer.voucher.dto.VoucherCreateRequest;
import com.example.woowa.customer.voucher.dto.VoucherFindResponse;
import com.example.woowa.customer.voucher.entity.Voucher;
import com.example.woowa.customer.voucher.enums.EventType;
import com.example.woowa.customer.voucher.enums.VoucherType;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VoucherMapperTest {
  @Autowired
  private VoucherMapper voucherMapper;

  @Test
  @DisplayName("쿠폰 dto 변환")
  void toVoucherDto() {
    Voucher voucher = new Voucher(VoucherType.FiXED, EventType.MONTH, 3000, LocalDateTime.now().plusDays(14));

    VoucherFindResponse voucherFindResponse = voucherMapper.toVoucherDto(voucher);

    Assertions.assertThat(voucherFindResponse.getDiscountValue()).isEqualTo(voucher.getDiscountValue());
    Assertions.assertThat(voucherFindResponse.getVoucherType()).isEqualTo(voucher.getVoucherType().toString());
    Assertions.assertThat(voucherFindResponse.getEventType()).isEqualTo(voucher.getEventType().toString());
    Assertions.assertThat(voucherFindResponse.getExpirationDate()).isEqualTo(voucher.getExpirationDate());
  }

  @Test
  @DisplayName("쿠폰 엔티티 변환")
  void toVoucher() {
    VoucherCreateRequest voucherCreateRequest = new VoucherCreateRequest("fixed", "month", 3000, "2023-12-01 12:00");

    Voucher voucher = voucherMapper.toVoucher(voucherCreateRequest);

    Assertions.assertThat(voucher.getVoucherType().toString()).isEqualTo(voucherCreateRequest.getVoucherType());
    Assertions.assertThat(voucher.getEventType().toString()).isEqualTo(voucherCreateRequest.getEventType());
    Assertions.assertThat(voucher.getDiscountValue()).isEqualTo(voucherCreateRequest.getDiscountValue());
    Assertions.assertThat(voucher.getIsUse()).isEqualTo(false);
  }
}