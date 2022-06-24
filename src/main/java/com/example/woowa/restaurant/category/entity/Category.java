package com.example.woowa.restaurant.category.entity;

import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
@Table(name = "category")
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<RestaurantCategory> restaurantCategories = new ArrayList<>();

    @Column(unique = true, nullable = false, length = 10)
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public void addRestaurantCategory(RestaurantCategory restaurantCategory) {
        if (restaurantCategory.getCategory() != this) {
            restaurantCategory.setCategory(this);
        }
    }

    public void changeCategoryName(String name) {
        this.name = name;
    }

}
