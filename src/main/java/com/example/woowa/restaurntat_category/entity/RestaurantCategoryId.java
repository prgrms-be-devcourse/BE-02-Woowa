package com.example.woowa.restaurntat_category.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
public class RestaurantCategoryId implements Serializable {

    private Long restaurantId;

    private Long categoryId;

}
