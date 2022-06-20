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
@Table(name = "review")
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "content", nullable = false)
  private String content;
  //길이 제한

  @Column(name = "score_type", nullable = false, length = 45)
  private String score_type;
  //문자 타입?
  //최소 1점 이상

  @ManyToOne
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  public Review(String content, String score_type, Customer customer) {
    assert ! content.isBlank();
    assert ! score_type.isBlank();
    assert customer != null;
    this.content = content;
    this.score_type = score_type;
    this.customer = customer;
  }

  public Review() {

  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    assert ! content.isBlank();
    this.content = content;
  }

  public String getScore_type() {
    return score_type;
  }

  public void setScore_type(String score_type) {
    assert ! score_type.isBlank();
    this.score_type = score_type;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    assert customer != null;
    this.customer = customer;
  }
}
