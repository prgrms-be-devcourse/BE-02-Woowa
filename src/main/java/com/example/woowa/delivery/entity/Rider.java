package com.example.woowa.delivery.entity;

import com.example.woowa.common.base.BaseLoginEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rider")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rider extends BaseLoginEntity {

    @OneToMany
    private final List<Delivery> deliveryList = new ArrayList<>();
    @OneToMany(mappedBy = "rider", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RiderAreaCode> riderAreaCodeList = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isDelivery;

    @Builder
    private Rider(String loginId, String loginPassword, String name, String phoneNumber) {
        super(loginId, loginPassword, name, phoneNumber);
        this.isDelivery = false;
    }

    public static Rider createRider(String loginId, String loginPassword, String name,
        String phoneNumber) {
        return new Rider(loginId, loginPassword, name, phoneNumber);
    }

    public void changeIsDelivery(boolean isDelivery) {
        this.isDelivery = isDelivery;
    }

    public void addDelivery(Delivery delivery) {
        this.deliveryList.add(delivery);
    }


    public void removeRiderAreaCode(RiderAreaCode riderAreaCode) {
        if (riderAreaCodeList.contains(riderAreaCode)) {
            this.riderAreaCodeList.remove(riderAreaCode);
        }
    }

    public void addRiderAreaCode(RiderAreaCode riderAreaCode) {
        this.riderAreaCodeList.add(riderAreaCode);
    }
}
