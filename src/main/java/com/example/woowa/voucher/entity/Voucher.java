package com.example.woowa.voucher.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.customer.entity.Customer;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voucher")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class Voucher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String voucherType;

  @Column(nullable = false)
  private int discountValue;

  @Column(nullable = false)
  private LocalDateTime expirationDate;

  @Column(columnDefinition = "TINYINT default false")
  private boolean isUse;

  @Column(nullable = false)
  private String code;

  @ManyToOne
  @JoinColumn
  private Customer customer;

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

  public void setCode(String code) {
    assert ! code.isBlank();
    this.code = code;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
}
