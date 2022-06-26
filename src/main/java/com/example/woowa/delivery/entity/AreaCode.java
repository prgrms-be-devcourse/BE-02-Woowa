package com.example.woowa.delivery.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AreaCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String defaultAddress;

    @Column(nullable = false)
    private boolean isAbolish;

    @OneToMany(mappedBy = "areaCode")
    private List<DeliveryArea> deliveryAreaList = new ArrayList<>();

    public AreaCode(String code, String defaultAddress, boolean isAbolish) {
        this.code = code;
        this.defaultAddress = defaultAddress;
        this.isAbolish = isAbolish;
    }

    public void addDeliveryArea(DeliveryArea deliveryArea) {
        this.deliveryAreaList.add(deliveryArea);
    }

    public String getAddress(String detailAddress) {
        return detailAddress + " " + detailAddress;
    }

}
