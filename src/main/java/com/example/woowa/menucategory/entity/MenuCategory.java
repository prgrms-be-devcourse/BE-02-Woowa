package com.example.woowa.menucategory.entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.restaurant.entity.Restaurant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false)
    private String title;

    private MenuCategory(Restaurant restaurant, String title) {
        this.restaurant = restaurant;
        this.title = title;
    }

    public MenuCategory createMenuCategory(Restaurant restaurant, String title) {
        restaurant.getMenuCategories().add(this);
        return new MenuCategory(restaurant, title);
    }

    public void changeTitle(String title) {
        this.title = title;
    }
}
