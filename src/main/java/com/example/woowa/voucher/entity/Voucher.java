package com.example.woowa.voucher.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
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
    this.voucherType = voucherType;
    this.discountValue = discountValue;
    this.expirationDate = expirationDate;
    this.code = code;
    this.couponCount = couponCount;
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
}
