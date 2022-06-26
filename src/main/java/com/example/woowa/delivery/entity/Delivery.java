package com.example.woowa.delivery.entity;

import com.example.woowa.customer.customer.entity.Customer;
import com.example.woowa.delivery.enums.DeliveryStatus;
import com.example.woowa.order.order.entity.Order;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

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

    @Column(nullable = false)
    private LocalDate arrivalTime;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    private Delivery(String restaurantAddress, String customerAddress, int deliveryFee, DeliveryStatus deliveryStatus) {
        this.restaurantAddress = restaurantAddress;
        this.customerAddress = customerAddress;
        this.deliveryFee = deliveryFee;
        this.deliveryStatus = deliveryStatus;
    }

    public static Delivery createDelivery(Order order, String restaurantAddress, String customerAddress, int deliveryFee) {
        return new Delivery(restaurantAddress, customerAddress, deliveryFee, DeliveryStatus.DELIVERY_WAITING);
    }

    private void changeDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }
}
