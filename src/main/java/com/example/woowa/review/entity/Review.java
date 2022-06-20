package com.example.woowa.review.entity;

import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.customer.entity.Customer;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String content;

  @Column(nullable = false, length = 45)
  private String scoreType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  private Customer customer;

  public Review(String content, String scoreType, Customer customer) {
    this.content = content;
    this.scoreType = scoreType;
    this.customer = customer;
  }

  public void setContent(String content) {
    assert ! content.isBlank();
    this.content = content;
  }

  public void setScoreType(String scoreType) {
    assert ! scoreType.isBlank();
    this.scoreType = scoreType;
  }
}
