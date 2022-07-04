package com.example.woowa.restaurant.menugroup.entity;

import com.example.woowa.common.base.BaseTimeEntity;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;

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
import org.springframework.util.StringUtils;

@Entity
@Table(name = "menu_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MenuGroup extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "menuGroup", cascade = CascadeType.REMOVE)
    private List<Menu> menus = new ArrayList<>();

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    private MenuGroup(Restaurant restaurant, String title, String description) {
        this.restaurant = restaurant;
        this.title = title;
        this.description = description;
    }

    public static MenuGroup createMenuGroup(Restaurant restaurant, String title,
            String description) {
        MenuGroup menuGroup = new MenuGroup(restaurant, title, getStoreDescription(description));
        restaurant.getMenuGroups().add(menuGroup);
        return menuGroup;
    }

    public void update(String title, String description) {
        this.title = title;
        this.description = getStoreDescription(description);
    }

    private static String getStoreDescription(String description) {
        return StringUtils.hasText(description) ? description : null;
    }

    public void addMenu(Menu menu) {
        menus.add(menu);
    }
}
