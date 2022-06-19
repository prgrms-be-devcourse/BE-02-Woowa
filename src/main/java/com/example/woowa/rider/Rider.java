package com.example.woowa.rider;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Rider {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /*
    배달 받는 위치 리스트.
     */
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
