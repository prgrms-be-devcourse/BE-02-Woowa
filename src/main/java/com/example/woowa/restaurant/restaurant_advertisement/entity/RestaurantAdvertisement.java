package com.example.woowa.restaurant.restaurant_advertisement.entity;

import com.example.woowa.restaurant.advertisement.entity.Advertisement;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(RestaurantAdvertisementId.class)
@Table(name = "restaurant_advertisement")
@Entity
public class RestaurantAdvertisement {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisement;

}
