package com.example.woowa.restaurant.restaurntat_category.entity;

import com.example.woowa.restaurant.category.entity.Category;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(RestaurantCategoryId.class)
@Table(name = "restaurant_category")
@Entity
public class RestaurantCategory {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public RestaurantCategory(Restaurant restaurant, Category category) {
        setRestaurant(restaurant);
        setCategory(category);
    }

    public void setRestaurant(Restaurant restaurant) {
        if (Objects.nonNull(this.restaurant)) {
            this.restaurant.getRestaurantCategories().remove(this);
        }
        this.restaurant = restaurant;
        this.restaurant.getRestaurantCategories().add(this);
    }

    public void setCategory(Category category) {
        if (Objects.nonNull(this.category)) {
            this.category.getRestaurantCategories().remove(this);
        }
        this.category = category;
        this.category.getRestaurantCategories().add(this);
    }

}