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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
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
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private DeliveryStatus deliveryStatus;

    private LocalDateTime arrivalTime;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;

    @Builder
    private Delivery(Order order, String restaurantAddress, String customerAddress, int deliveryFee,
        DeliveryStatus deliveryStatus) {
        this.order = order;
        this.restaurantAddress = restaurantAddress;
        this.customerAddress = customerAddress;
        this.deliveryFee = deliveryFee;
        this.deliveryStatus = deliveryStatus;
    }

    public static Delivery createDelivery(Order order, String restaurantAddress,
        String customerAddress, int deliveryFee) {
        return new Delivery(order, restaurantAddress, customerAddress, deliveryFee,
            DeliveryStatus.DELIVERY_WAITING);
    }

    public void accept(Rider rider, int cookMinute, int deliveryMinute) {
        this.rider = rider;
        this.deliveryStatus = DeliveryStatus.DELIVERY_REGISTRATION;
        this.arrivalTime = LocalDateTime.now().plusMinutes(deliveryMinute + cookMinute);
    }

    public void delay(int delayMinute) {
        this.arrivalTime = arrivalTime.plusMinutes(delayMinute);
    }

    public void pickUp(int deliveryMinute) {
        this.deliveryStatus = DeliveryStatus.DELIVERY_PICKUP;
        this.arrivalTime = LocalDateTime.now().plusMinutes(deliveryMinute);
    }

    public void finish() {
        this.deliveryStatus = DeliveryStatus.DELIVERY_FINISH;
        this.arrivalTime = LocalDateTime.now();
    }
}
