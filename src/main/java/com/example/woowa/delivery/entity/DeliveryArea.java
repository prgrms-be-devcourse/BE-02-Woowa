package com.example.woowa.delivery.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "delivery_area")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String areaCode;

    @Column(nullable = false)
    private Integer deliveryPrice;
}
