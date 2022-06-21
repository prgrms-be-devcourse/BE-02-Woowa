package com.example.woowa.restaurant_advertisement.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
public class RestaurnatAdvertisementId implements Serializable {

    private Long restaurantId;

    private Long advertisementId;

}
