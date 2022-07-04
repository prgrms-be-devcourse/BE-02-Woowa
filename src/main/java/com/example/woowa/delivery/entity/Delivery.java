package com.example.woowa.delivery.entity;

import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.order.order.entity.Order;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "delivery")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String restaurantAddress;

    @Column(nullable = false)
    private String customerAddress;

    @Column(nullable = false)
    private int deliveryFee;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private LocalDateTime arrivalTime;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    private Delivery(String restaurantAddress, String customerAddress, int deliveryFee,
            DeliveryStatus deliveryStatus) {
        this.restaurantAddress = restaurantAddress;
        this.customerAddress = customerAddress;
        this.deliveryFee = deliveryFee;
        this.deliveryStatus = deliveryStatus;
    }

    public static Delivery createDelivery(Order order, String restaurantAddress,
            String customerAddress, int deliveryFee) {
        Delivery delivery = new Delivery(restaurantAddress, customerAddress, deliveryFee,
                DeliveryStatus.DELIVERY_WAITING);
        order.setDelivery(delivery);
        return delivery;
    }

    private void changeDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
