package com.example.woowa.customer.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

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
@Table(name = "customer_address")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class CustomerAddress {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 45)
  private String address;

  @Column(nullable = false, length = 45)
  private String nickname;

  @Column(nullable = false, length = 45)
  private String areaCode;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Customer customer;

  public void setAddress(String address) {
    assert !address.isBlank();
    this.address = address;
  }

  public void setNickname(String nickname) {
    assert ! nickname.isBlank();
    this.nickname = nickname;
  }

  public void setAreaCode(String areaCode) {
    assert ! areaCode.isBlank();
    this.areaCode = areaCode;
  }

  public void setCustomer(Customer customer) {
    assert customer != null;
    this.customer = customer;
  }
}
