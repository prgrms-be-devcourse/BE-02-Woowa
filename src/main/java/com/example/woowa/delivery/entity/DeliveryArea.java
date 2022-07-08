package com.example.woowa.delivery.entity;

import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        areaCode.addDeliveryArea(this);
        restaurant.addDeliveryArea(this);
    }

    public void setRestaurant(Restaurant restaurant) {
        if (Objects.nonNull(this.restaurant)) {
            this.restaurant.getDeliveryAreas().remove(this);
        }
        this.restaurant = restaurant;
        this.restaurant.getDeliveryAreas().add(this);
    }

}
