package com.example.woowa.customer.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

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
@Table(name = "customer_grade")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class CustomerGrade {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private Integer orderCount;

  @Column(nullable = false, length = 45)
  private String grade;

  @Column(nullable = false)
  private Integer discountPrice;

  public void setOrder_count(int orderCount) {
    assert orderCount > 0;
    this.orderCount = orderCount;
  }

  public void setGrade(String grade) {
    assert ! grade.isBlank();
    this.grade = grade;
  }

  public void setDiscount_price(int discountPrice) {
    assert discountPrice > 0;
    this.discountPrice = discountPrice;
  }
}
