package com.example.woowa.restaurant.menu.entity;

import com.example.woowa.restaurant.menu.enums.SaleStatus;
import com.example.woowa.restaurant.menucategory.entity.MenuCategory;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_category_id", nullable = false)
    private MenuCategory menuCategory;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer price;

    private String description;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isMain;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SaleStatus saleStatus;

    private Menu(MenuCategory menuCategory, String title, Integer price, String description,
            Boolean isMain,
            SaleStatus saleStatus) {
        this.menuCategory = menuCategory;
        this.title = title;
        this.price = price;
        this.description = description;
        this.isMain = isMain;
        this.saleStatus = saleStatus;
    }

    public static Menu createMenu(MenuCategory menuCategory, String title, Integer price,
            String description,
            Boolean isMain,
            SaleStatus saleStatus) {
        return new Menu(menuCategory, title, price, description, isMain, saleStatus);
    }

    public void change(MenuCategory menuCategory, String title, Integer price, String description) {
        if (Objects.nonNull(this.menuCategory)) {
            menuCategory.getMenus().remove(this);
        }

        menuCategory.getMenus().add(this);
        this.menuCategory = menuCategory;
        this.title = title;
        this.price = price;
        this.description = description;
    }

    public void changeSaleStatus(SaleStatus saleStatus) {
        this.saleStatus = saleStatus;
    }

    public void setMainMenu(Boolean isMain) {
        this.isMain = isMain;
    }
}
