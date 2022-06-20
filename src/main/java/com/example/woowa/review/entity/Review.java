package com.example.woowa.review.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.customer.entity.Customer;
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
@Table(name = "review")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false, length = 45)
  private String scoreType;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Customer customer;

  public void setContent(String content) {
    assert ! content.isBlank();
    this.content = content;
  }

  public void setScoreType(String scoreType) {
    assert ! scoreType.isBlank();
    this.scoreType = scoreType;
  }

  public void setCustomer(Customer customer) {
    assert customer != null;
    this.customer = customer;
  }
}
