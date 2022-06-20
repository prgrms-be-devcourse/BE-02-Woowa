package com.example.woowa.rider.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rider")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String loginPassword;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private Boolean isDelivery;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany
    private List<Delivery> deliveryList = new ArrayList<>();

    @OneToMany
    private List<DeliveryArea> deliveryAreaList = new ArrayList<>();

    public Rider(String loginId, String loginPassword, Boolean isDelivery, String name, String phoneNumber) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.isDelivery = isDelivery;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void changeIsDelivery(boolean isDelivery) {
        this.isDelivery = isDelivery;
    }

    public void addDelivery(Delivery delivery) {
        this.deliveryList.add(delivery);
    }
}
