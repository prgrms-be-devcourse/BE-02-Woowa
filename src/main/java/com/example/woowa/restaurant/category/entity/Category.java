package com.example.woowa.restaurant.category.entity;

import com.example.woowa.common.base.BaseTimeEntity;
import com.example.woowa.restaurant.restaurntat_category.entity.RestaurantCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
@Entity
public class Category extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RestaurantCategory> restaurantCategories = new ArrayList<>();

    @Column(unique = true, nullable = false, length = 10)
    private String name;

    @Builder
    public Category(String name) {
        this.name = name;
    }

    public void addRestaurantCategory(RestaurantCategory restaurantCategory) {
        if (!Objects.equals(restaurantCategory.getCategory().getId(), this.getId())) {
            restaurantCategory.setCategory(this);
        }
    }

    public void changeName(String name) {
        this.name = name;
    }

}
