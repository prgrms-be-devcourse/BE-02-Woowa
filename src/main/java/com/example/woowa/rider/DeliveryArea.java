package com.example.woowa.rider;

import lombok.Getter;

import javax.persistence.*;

@Table
@Entity
@Getter
public class DeliveryArea {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AreaCode areaCode;

    @Column(nullable = false)
    private Integer deliveryPrice;
}
