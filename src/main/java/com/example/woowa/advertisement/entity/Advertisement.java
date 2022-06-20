package com.example.woowa.advertisement.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "advertisement")
@Entity
public class Advertisement {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "advertise_type", unique = true, nullable = false, length = 10)
    private String advertiseType;

    @Column(nullable = false)
    private Integer price;

    @Lob
    private String description;

    public Advertisement(String advertiseType, Integer price, String description) {
        this.advertiseType = advertiseType;
        this.price = price;
        this.description = description;
    }

    public void changeAdvertiseType(String advertiseType) {
        this.advertiseType = advertiseType;
    }

    public void changePrice(Integer price) {
        this.price = price;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

}
