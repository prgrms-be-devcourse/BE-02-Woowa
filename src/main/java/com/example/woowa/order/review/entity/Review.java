package com.example.woowa.order.review.entity;

import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.order.review.enums.ScoreType;
import com.example.woowa.customer.customer.entity.Customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private ScoreType scoreType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Customer customer;

    public Review(String content, int scoreType, Customer customer) {
        this.content = content;
        this.scoreType = ScoreType.of(scoreType);
        this.customer = customer;
    }

    public int getScoreType() {
        return this.scoreType.getValue();
    }

    public void setContent(String content) {
        assert !content.isBlank();
        this.content = content;
    }

    public void setScoreType(int scoreType) {
        this.scoreType = ScoreType.of(scoreType);
    }
}
