package com.example.woowa.customer.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.review.entity.Review;
import com.example.woowa.voucher.entity.Voucher;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 45)
  private String loginId;

  @Column(nullable = false, length = 45)
  private String loginPassword;

  @Column(columnDefinition = "INT default 0")
  private Integer orderPerMonth;

  @Column(nullable = false, length = 45)
  private String birthdate;

  @Column(columnDefinition = "INT default 0")
  private Integer point;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private CustomerGrade customerGrade;
  //초기 등급

  @OneToMany(mappedBy = "customer")
  private List<Review> reviews;
  //초기 default value 빈 배열

  @OneToMany(mappedBy = "customer")
  private List<CustomerAddress> customerAddresses;

  @OneToMany(mappedBy = "customer")
  private List<Voucher> vouchers;

  public void setLoginId(String login_id) {
    assert ! login_id.isBlank();
    this.loginId = login_id;
  }

  public void setLoginPassword(String login_password) {
    assert ! login_password.isBlank();
    this.loginPassword = login_password;
  }

  public void initOrderPerMonth() {
    this.orderPerMonth = 0;
  }

  public void addOrderPerMonth() {
    this.orderPerMonth += 1;
  }

  public void setBirthdate(String birthdate) {
    assert ! birthdate.isBlank();
    this.birthdate = birthdate;
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
