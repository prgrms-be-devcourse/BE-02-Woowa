package com.example.woowa.rider;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Getter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String restaurantAddress;

    @Column(nullable = false)
    private String customerAddress;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private DeliveryStatus deliveryStatus;

    @Column(nullable = false)
    private LocalDate arrivalTime;
}
