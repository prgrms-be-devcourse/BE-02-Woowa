package com.example.woowa.menucategory.entity;

import com.example.woowa.menu.entity.Menu;
import com.example.woowa.restaurant.entity.Restaurant;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MenuCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menuCategory", cascade = CascadeType.REMOVE)
    private List<Menu> menus = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    private MenuCategory(Restaurant restaurant, String title) {
        this.restaurant = restaurant;
        this.title = title;
    }

    public static MenuCategory createMenuCategory(Restaurant restaurant, String title) {
        MenuCategory menuCategory = new MenuCategory(restaurant, title);
        restaurant.getMenuCategories().add(menuCategory);
        return menuCategory;
    }

    public void changeTitle(String title) {
        this.title = title;
    }
}
