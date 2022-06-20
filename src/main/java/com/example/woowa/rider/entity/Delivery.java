package com.example.woowa.rider.entity;

import com.example.woowa.rider.enums.DeliveryStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
