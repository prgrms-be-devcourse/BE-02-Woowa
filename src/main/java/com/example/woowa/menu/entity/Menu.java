package com.example.woowa.menu.entity;

import com.example.woowa.menu.enums.SaleStatus;
import com.example.woowa.menucategory.entity.MenuCategory;
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
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_category_id")
    private MenuCategory menuCategory;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer price;

    private String description;

    @Column(nullable = false)
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

    public Menu createMenu(MenuCategory menuCategory, String title, Integer price,
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
