package com.example.woowa.restaurant_advertisement.entity;

import com.example.woowa.advertisement.entity.Advertisement;
import com.example.woowa.restaurant.entity.Restaurant;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(RestaurnatAdvertisementId.class)
@Table(name = "restaurant_advertisement")
@Entity
public class RestaurantAdvertisement {

    @Id
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurantId;

    @Id
    @ManyToOne
    @JoinColumn(name = "advertisement_id")
    private Advertisement advertisementId;

}
