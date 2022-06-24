package com.example.woowa.restaurant.restaurant.entity;

import com.example.woowa.delivery.entity.AreaCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "delivery_area")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeliveryArea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private AreaCode areaCode;

    @Column(nullable = false)
    private int deliveryFee;

    public DeliveryArea(AreaCode areaCode) {
        this.areaCode = areaCode;
        this.deliveryFee = 0;
    }

    public DeliveryArea(AreaCode areaCode, int deliveryFee) {
        this.areaCode = areaCode;
        this.deliveryFee = deliveryFee;
    }
}
