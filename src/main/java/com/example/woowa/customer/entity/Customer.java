package com.example.woowa.customer.entity;

import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.review.entity.Review;
import com.example.woowa.voucher.entity.Voucher;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 45)
  private String loginId;

  @Column(nullable = false, length = 45)
  private String loginPassword;

  @Column(columnDefinition = "INT DEFAULT 0")
  private Integer orderPerMonth;

  @Column(nullable = false, length = 45)
  private String birthdate;

  @Column(columnDefinition = "INT DEFAULT 0")
  private Integer point;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_grade_id", nullable = false)
  private CustomerGrade customerGrade;

  @OneToMany(mappedBy = "customer")
  private List<Review> reviews = new ArrayList<>();

  @OneToMany(mappedBy = "customer")
  private List<CustomerAddress> customerAddresses = new ArrayList<>();

  @OneToMany
  private List<Voucher> vouchers = new ArrayList<>();

  public Customer(String loginId, String loginPassword, String birthdate,
      CustomerGrade customerGrade) {
    assert ! loginId.isBlank();
    assert ! loginPassword.isBlank();
    assert ! birthdate.isBlank();
    assert customerGrade != null;
    this.loginId = loginId;
    this.loginPassword = loginPassword;
    this.birthdate = birthdate;
    this.customerGrade = customerGrade;
  }

  public void setLoginPassword(String loginPassword) {
    assert ! loginPassword.isBlank();
    this.loginPassword = loginPassword;
  }

  public void initOrderPerMonth() {
    this.orderPerMonth = 0;
  }

  public void addOrderPerMonth() {
    this.orderPerMonth += 1;
  }

  public void usePoint(int point) {
    assert point <= this.point;
    this.point -= point;
  }

  public void addPoint(int point) {
    this.point += point;
  }

  public void setCustomerGrade(CustomerGrade customerGrade) {
    this.customerGrade = customerGrade;
  }

  public void addReview(Review review) {
    this.reviews.add(review);
  }

  public void removeReview(Review review) {
    this.reviews.remove(review);
  }

  public void addCustomerAddress(CustomerAddress customerAddress) {
    this.customerAddresses.add(customerAddress);
  }

  public void removeCustomerAddress(CustomerAddress customerAddress) {
    this.customerAddresses.remove(customerAddress);
  }

  public void addVoucher(Voucher voucher) {
    this.vouchers.add(voucher);
  }

  public void removeVoucher(Voucher voucher) {
    this.vouchers.remove(voucher);
  }
}
