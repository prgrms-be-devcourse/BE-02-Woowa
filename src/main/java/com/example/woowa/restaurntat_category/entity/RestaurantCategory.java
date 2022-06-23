package com.example.woowa.restaurntat_category.entity;

import com.example.woowa.category.entity.Category;
import com.example.woowa.restaurant.entity.Restaurant;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(RestaurantCategoryId.class)
@Table(name = "restaurant_category")
@Entity
public class RestaurantCategory {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

}
