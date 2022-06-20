package com.example.woowa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer_address")
public class CustomerAddress {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "address", nullable = false, length = 45)
  private String address;

  @Column(name = "nickname", nullable = false, length = 45)
  private String nickname;

  @Column(name = "area_code", nullable = false, length = 45)
  private String area_code;
  //길이 제한

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  public CustomerAddress(String address, String nickname, String area_code,
      Customer customer) {
    assert ! address.isBlank();
    assert ! nickname.isBlank();
    assert ! area_code.isBlank();
    assert customer != null;
    this.address = address;
    this.nickname = nickname;
    this.area_code = area_code;
    this.customer = customer;
  }

  public CustomerAddress() {

  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    assert ! address.isBlank();
    this.address = address;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    assert ! nickname.isBlank();
    this.nickname = nickname;
  }

  public String getArea_code() {
    return area_code;
  }

  public void setArea_code(String area_code) {
    assert ! area_code.isBlank();
    this.area_code = area_code;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    assert customer != null;
    this.customer = customer;
  }
}
