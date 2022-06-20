package com.example.woowa.restaurant.entity;

import com.example.woowa.menucategory.entity.MenuCategory;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "restaurant")
    private List<MenuCategory> menuCategories = new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String businessNumber;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime closingTime;

    @Column(nullable = false)
    private Boolean isOpen;

    @Column(nullable = false)
    private String phoneNumber;

    private String description;

    private Double averageReviewScore;

    private Integer reviewCount;

    @Column(nullable = false)
    private String address;

    public Restaurant(String name, String businessNumber, LocalTime openingTime,
            LocalTime closingTime,
            Boolean isOpen, String phoneNumber, String description, String address) {
        this.name = name;
        this.businessNumber = businessNumber;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.isOpen = isOpen;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.address = address;
        this.reviewCount = 0;
        this.averageReviewScore = 0.0D;
    }

    public Restaurant createRestaurant(String name, String businessNumber,
            LocalTime openingTime, LocalTime closingTime, Boolean isOpen, String phoneNumber,
            String description, String address) {
        return new Restaurant(name, businessNumber, openingTime, closingTime, isOpen, phoneNumber,
                description, address);
    }

    public void changeOpiningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public void changeClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public void changeOpenStatus(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public void changeReviewInfo(Double averageReviewScore, Integer reviewCount) {
        this.averageReviewScore = averageReviewScore;
        this.reviewCount = reviewCount;
    }
}
