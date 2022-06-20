package com.example.woowa;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "customer")
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "login_id", unique = true, length = 45)
  private String login_id;
  //영숫자 조합만 허용

  @Column(name = "login_password", nullable = false, length = 45)
  private String login_password;
  //아이디 포함 X
  //영어, 숫자, 특수문자 조합만 허용

  @Column(name = "order_per_month")
  private int order_per_month;
  //초기값 0

  @Column(name = "birthdate", nullable = false, length = 45)
  private String birthdate;
  //1800-01-01

  @Column(name = "point")
  private int point;
  //초기값 0 양수

  @OneToOne
  @JoinColumn(name = "customer_grade", nullable = false)
  private CustomerGrade customer_grade;
  //초기 등급

  @OneToMany(mappedBy = "customer")
  private List<Review> reviews;

  @OneToMany(mappedBy = "customer")
  private List<CustomerAddress> customerAddresses;

  @OneToMany(mappedBy = "customer")
  private List<Voucher> vouchers;

  public Customer(String login_id, String login_password, String birthdate) {
    assert ! login_id.isBlank();
    assert ! login_password.isBlank();
    assert ! birthdate.isBlank();
    this.login_id = login_id;
    this.login_password = login_password;
    this.order_per_month = 0;
    this.birthdate = birthdate;
    this.point = 0;
    this.reviews = new ArrayList<>();
    this.customerAddresses = new ArrayList<>();
    this.vouchers = new ArrayList<>();
  }

  public Customer() {

  }

  public String getLogin_id() {
    return login_id;
  }

  public void setLogin_id(String login_id) {
    assert ! login_id.isBlank();
    this.login_id = login_id;
  }

  public String getLogin_password() {
    return login_password;
  }

  public void setLogin_password(String login_password) {
    assert ! login_password.isBlank();
    this.login_password = login_password;
  }

  public int getOrder_per_month() {
    return order_per_month;
  }

  public void initOrder_per_month() {
    this.order_per_month = 0;
  }

  public void addOrder_per_month() {
    this.order_per_month += 1;
  }

  public String getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(String birthdate) {
    assert ! birthdate.isBlank();
    this.birthdate = birthdate;
  }

  public int getPoint() {
    return point;
  }

  public void usePoint(int point) {
    assert point <= this.point;
    this.point -= point;
  }

  public void addPoint(int point) {
    this.point += point;
  }

  public CustomerGrade getCustomer_grade() {
    return customer_grade;
  }

  public void setCustomer_grade(CustomerGrade customer_grade) {
    this.customer_grade = customer_grade;
  }

  public List<Review> getReviews() {
    return reviews;
  }

  public void addReview(Review review) {
    this.reviews.add(review);
  }

  public void removeReview(Review review) {
    this.reviews.remove(review);
  }

  public List<CustomerAddress> getCustomerAddresses() {
    return customerAddresses;
  }

  public void addCustomerAddress(CustomerAddress customerAddress) {
    this.customerAddresses.add(customerAddress);
  }

  public void removeCustomerAddress(CustomerAddress customerAddress) {
    this.customerAddresses.remove(customerAddress);
  }

  public List<Voucher> getVouchers() {
    return vouchers;
  }

  public void addVoucher(Voucher voucher) {
    this.vouchers.add(voucher);
  }

  public void removeVoucher(Voucher voucher) {
    this.vouchers.remove(voucher);
  }
}
