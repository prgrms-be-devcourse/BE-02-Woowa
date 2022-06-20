package com.example.woowa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customer_grade")
public class CustomerGrade {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "order_count", nullable = false)
  private int order_count;
  //양수

  @Column(name = "grade", nullable = false, length = 45)
  private String grade;

  @Column(name = "discount_price", nullable = false)
  private int discount_price;
  //양수

  public CustomerGrade(int order_count, String grade, int discount_price) {
    assert order_count > 0;
    assert ! grade.isBlank();
    assert discount_price > 0;
    this.order_count = order_count;
    this.grade = grade;
    this.discount_price = discount_price;
  }

  public CustomerGrade() {

  }

  public int getOrder_count() {
    return order_count;
  }

  public void setOrder_count(int order_count) {
    assert order_count > 0;
    this.order_count = order_count;
  }

  public String getGrade() {
    return grade;
  }

  public void setGrade(String grade) {
    assert ! grade.isBlank();
    this.grade = grade;
  }

  public int getDiscount_price() {
    return discount_price;
  }

  public void setDiscount_price(int discount_price) {
    assert discount_price > 0;
    this.discount_price = discount_price;
  }
}
