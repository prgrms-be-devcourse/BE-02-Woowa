package com.example.woowa.restaurant_advertisement.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RestaurnatAdvertisementId implements Serializable {

    private Long restaurant;

    private Long advertisement;

}
