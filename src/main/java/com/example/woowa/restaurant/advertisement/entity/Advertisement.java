package com.example.woowa.restaurant.advertisement.entity;

import com.example.woowa.restaurant.advertisement.converter.UnitTypeConverter;
import com.example.woowa.restaurant.advertisement.converter.RateTypeConverter;
import com.example.woowa.restaurant.advertisement.enums.RateType;
import com.example.woowa.restaurant.advertisement.enums.UnitType;
import com.example.woowa.restaurant.restaurant_advertisement.entity.RestaurantAdvertisement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "advertisement")
@Entity
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "advertisement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestaurantAdvertisement> restaurantAdvertisements = new ArrayList<>();

    @Column(unique = true, nullable = false, length = 10)
    private String title;

    @Convert(converter = UnitTypeConverter.class)
    @Column(nullable = false)
    private UnitType unitType;

    @Convert(converter = RateTypeConverter.class)
    @Column(nullable = false)
    private RateType rateType;

    @Column(nullable = false)
    private Integer rate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer limitSize;

    @Column(nullable = false)
    private Integer currentSize;

    public Advertisement(String title, UnitType unitType, RateType rateType, Integer rate,
        String description, Integer limitSize) {
        this.title = title;
        this.unitType = unitType;
        this.rateType = rateType;
        this.rate = rate;
        this.description = description;
        this.limitSize = limitSize;
        this.currentSize = 0;
    }

    public void addRestaurantAdvertisement(RestaurantAdvertisement restaurantAdvertisement) {
        if (!Objects.equals(restaurantAdvertisement.getAdvertisement().getId(), this.getId())) {
            restaurantAdvertisement.setAdvertisement(this);
        }
    }

    public void removeRestaurantAdvertisement(RestaurantAdvertisement restaurantAdvertisement) {
        if (Objects.equals(restaurantAdvertisement.getAdvertisement().getId(), this.getId())) {
            this.getRestaurantAdvertisements().remove(restaurantAdvertisement);
            this.decrementCurrentSize();
        }
    }

    public void changeTitle(String title) {
        this.title = title;
    }

    public void changeUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public void changeRateType(RateType rateType) {
        this.rateType = rateType;
    }

    public void changeRate(Integer rate) {
        this.rate = rate;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeLimitSize(Integer limitSize) {
        this.limitSize = limitSize;
    }

    public void incrementCurrentSize() {
        this.currentSize++;
    }

    public void decrementCurrentSize() {
        this.currentSize--;
    }

    public static boolean isAvailable(Advertisement advertisement) {
        return advertisement.currentSize < advertisement.limitSize;
    }

}
