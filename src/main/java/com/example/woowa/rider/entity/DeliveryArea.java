package com.example.woowa.rider.entity;

import lombok.Getter;

import javax.persistence.*;

@Table
@Entity
@Getter
public class DeliveryArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String areaCode;

    @Column(nullable = false)
    private Integer deliveryPrice;
}
