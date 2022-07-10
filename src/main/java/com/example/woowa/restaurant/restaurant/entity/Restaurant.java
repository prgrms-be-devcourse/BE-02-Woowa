package com.example.woowa.restaurant.restaurant.entity;


import com.example.woowa.common.base.BaseTimeEntity;
import com.example.woowa.delivery.entity.DeliveryArea;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.owner.entity.Owner;
import com.example.woowa.restaurant.restaurant_advertisement.entity.RestaurantAdvertisement;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategory;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "restaurant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Restaurant extends BaseTimeEntity {

    @OneToMany(mappedBy = "restaurant")
    private final List<MenuGroup> menuGroups = new ArrayList<>();
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RestaurantCategory> restaurantCategories = new ArrayList<>();
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)

    private final List<RestaurantAdvertisement> restaurantAdvertisements = new ArrayList<>();
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DeliveryArea> deliveryAreas = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

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

    @Column(nullable = false)
    private Boolean isPermitted;

    private Restaurant(String name, String businessNumber, LocalTime openingTime,
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
        this.isPermitted = false;
    }

    public static Restaurant createRestaurant(String name, String businessNumber,
        LocalTime openingTime, LocalTime closingTime, Boolean isOpen, String phoneNumber,
        String description, String address) throws IllegalArgumentException {
        validateBusinessHours(openingTime, closingTime);
        if (!CRNValidator.isValid(businessNumber)) {
            throw new IllegalArgumentException("잘못된 사업자등록번호입니다.");
        }

        return new Restaurant(name, businessNumber, openingTime, closingTime, isOpen, phoneNumber,
            description, address);
    }

    public void updateBusinessHours(LocalTime openingTime, LocalTime closingTime)
        throws IllegalArgumentException {
        validateBusinessHours(openingTime, closingTime);
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public void openRestaurant() {
        this.isOpen = true;
    }

    public void closeRestaurant() {
        this.isOpen = false;
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

    public void addDeliveryArea(DeliveryArea deliveryArea) {
        deliveryAreas.add(deliveryArea);
    }

    public void addRestaurantCategory(RestaurantCategory restaurantCategory) {
        restaurantCategory.setRestaurant(this);
    }

    public void setPermitted() {
        this.isPermitted = true;
    }

    public void setOwner(Owner owner) {
        if (Objects.nonNull(this.owner)) {
            this.owner.getRestaurants().remove(this);
        }
        this.owner = owner;
        this.owner.getRestaurants().add(this);
    }

    private static void validateBusinessHours(LocalTime openingTime, LocalTime closingTime)
        throws IllegalArgumentException {
        if (closingTime.equals(openingTime)) {
            throw new IllegalArgumentException("openingTime 과 closingTime 은 같을 수 없습니다.");
        }
    }
}
