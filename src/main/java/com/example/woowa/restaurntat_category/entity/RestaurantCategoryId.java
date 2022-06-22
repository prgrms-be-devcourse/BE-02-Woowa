package com.example.woowa.restaurntat_category.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantCategoryId implements Serializable {

    private Long restaurant;

    private Long category;

}
