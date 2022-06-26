package com.example.woowa.delivery.entity;

import com.example.woowa.common.base.BaseLoginEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rider")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rider extends BaseLoginEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDelivery;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany
    private List<Delivery> deliveryList = new ArrayList<>();

    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RiderAreaCode> riderAreaCodeList = new ArrayList<>();

    public Rider(String loginId, String loginPassword, String name, String phoneNumber) {
        super(loginId, loginPassword);
        this.isDelivery = false;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public void changeIsDelivery(boolean isDelivery) {
        this.isDelivery = isDelivery;
    }

    public void addDelivery(Delivery delivery) {
        this.deliveryList.add(delivery);
    }
    public void addRiderAreaCode(RiderAreaCode riderAreaCode) {
        this.riderAreaCodeList.add(riderAreaCode);
    }

}
