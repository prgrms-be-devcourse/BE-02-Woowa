package com.example.woowa.restaurant.restaurant_advertisement.entity;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RestaurantAdvertisementId implements Serializable {

    private Long restaurant;

    private Long advertisement;

}
