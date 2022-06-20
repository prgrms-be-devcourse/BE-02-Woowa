package com.example.woowa.voucher.entity;

import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voucher")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Voucher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String voucherType;

  @Column(nullable = false)
  private Integer discountValue;

  @Column(nullable = false)
  private LocalDateTime expirationDate;

  @Column(columnDefinition = "TINYINT DEFAULT FALSE")
  private Boolean isUse;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private Integer couponCount;

  public Voucher(String voucherType, Integer discountValue, LocalDateTime expirationDate,
      String code, Integer couponCount) {
    assert isValidVoucherType(voucherType, discountValue);
    assert expirationDate != null;
    assert ! code.isBlank();
    assert couponCount > 0;
    this.voucherType = voucherType;
    this.discountValue = discountValue;
    this.expirationDate = expirationDate;
    this.code = code;
    this.couponCount = couponCount;
  }

  public boolean isValidVoucherType(String voucherType, Integer discountValue) {
    return (voucherType.equals("fixed") && (discountValue > 0)) || (voucherType.equals("percent") && (discountValue <= 100) && (discountValue > 0));
  }

  public void setVoucherType(String voucherType) {
    assert ! voucherType.isBlank();
    this.voucherType = voucherType;
  }

  public void setDiscountValue(int discountValue) {
    assert discountValue > 0;
    this.discountValue = discountValue;
  }

  public void setExpirationDate(LocalDateTime expirationDate) {
    assert expirationDate.isAfter(LocalDateTime.now());
    this.expirationDate = expirationDate;
  }

  public double useVoucher(double price) {
    assert price > 0;
    double discountResult = 0d;
    if (voucherType.equals("fixed")) {
      assert price >= discountValue;
      discountResult = price - discountValue;
    }
    else if (voucherType.equals("percent")) {
      discountResult = price * discountValue / 100;
    }
    return discountResult;
  }
}
