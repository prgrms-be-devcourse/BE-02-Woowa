package com.example.woowa.delivery.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AreaCode {

    @OneToMany(mappedBy = "areaCode", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<DeliveryArea> deliveryAreaList = new ArrayList<>();
    @OneToMany(mappedBy = "areaCode", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<RiderAreaCode> riderAreaCodeList = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String code;
    @Column(nullable = false)
    private String defaultAddress;
    @Column(nullable = false)
    private boolean isAbolish;

    public AreaCode(String code, String defaultAddress, boolean isAbolish) {
        this.code = code;
        this.defaultAddress = defaultAddress;
        this.isAbolish = isAbolish;
    }

    public void addDeliveryArea(DeliveryArea deliveryArea) {
        this.deliveryAreaList.add(deliveryArea);
    }

    public void addRiderArea(RiderAreaCode riderAreaCode) {
        this.riderAreaCodeList.add(riderAreaCode);
    }
}
