package com.example.woowa;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "voucher")
public class Voucher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "voucher_type", nullable = false)
  private String voucher_type;
  //고정 할인 타입만 존재

  @Column(name = "discount_value", nullable = false)
  private int discount_value;
  //양수

  @Column(name = "expiration_date", nullable = false)
  private LocalDateTime expiration_date;
  //만료인 경우 사용 안됨

  @Column(name = "is_use")
  private boolean is_use;
  //초기값 false

  @Column(name = "code", nullable = false)
  private String code;
  //길이 제한

  //  @OneToOne
  //  @JoinColumn(name = "order_id")
  //  private Order order;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  public Voucher(String voucher_type, int discount_value, LocalDateTime expiration_date,
      String code) {
    assert ! voucher_type.isBlank();
    assert discount_value > 0;
    assert expiration_date.isAfter(LocalDateTime.now());
    assert ! code.isBlank();
    this.voucher_type = voucher_type;
    this.discount_value = discount_value;
    this.expiration_date = expiration_date;
    this.is_use = false;
    this.code = code;
  }

  public Voucher() {

  }

  public String getVoucher_type() {
    return voucher_type;
  }

  public void setVoucher_type(String voucher_type) {
    assert ! voucher_type.isBlank();
    this.voucher_type = voucher_type;
  }

  public int getDiscount_value() {
    return discount_value;
  }

  public void setDiscount_value(int discount_value) {
    assert discount_value > 0;
    this.discount_value = discount_value;
  }

  public LocalDateTime getExpiration_date() {
    return expiration_date;
  }

  public void setExpiration_date(LocalDateTime expiration_date) {
    assert expiration_date.isAfter(LocalDateTime.now());
    this.expiration_date = expiration_date;
  }

  public boolean isIs_use() {
    return is_use;
  }

  public void setIs_use(boolean is_use) {
    this.is_use = is_use;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    assert ! code.isBlank();
    this.code = code;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
}
