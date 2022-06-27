package com.example.woowa.delivery.entity;

import com.example.woowa.delivery.entity.AreaCode;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_code_id", nullable = false)
    private AreaCode areaCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private int deliveryFee;

    public DeliveryArea(AreaCode areaCode, Restaurant restaurant) {
        this.areaCode = areaCode;
        this.restaurant = restaurant;
        this.deliveryFee = 0;
    }

    public DeliveryArea(AreaCode areaCode, Restaurant restaurant, int deliveryFee) {
        this.areaCode = areaCode;
        this.restaurant = restaurant;
        this.deliveryFee = deliveryFee;
    }
}
