package com.example.woowa.delivery.entity;

import com.example.woowa.delivery.enums.DeliveryStatus;
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

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition="BOOLEAN DEFAULT FALSE")
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false)
    private LocalDate arrivalTime;
}
