package com.example.woowa.restaurant.menu.entity;

import com.example.woowa.restaurant.menu.enums.SaleStatus;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
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
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

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

    private Menu(MenuGroup menuGroup, String title, Integer price, String description,
            Boolean isMain,
            SaleStatus saleStatus) {
        this.menuGroup = menuGroup;
        this.title = title;
        this.price = price;
        this.description = description;
        this.isMain = isMain;
        this.saleStatus = saleStatus;
    }

    public static Menu createMenu(MenuGroup menuGroup, String title, Integer price,
            String description,
            Boolean isMain,
            SaleStatus saleStatus) {
        Menu menu = new Menu(menuGroup, title, price, description, isMain, saleStatus);
        menuGroup.addMenu(menu);
        return menu;
    }

    public void change(MenuGroup menuGroup, String title, Integer price, String description) {
        if (Objects.nonNull(this.menuGroup)) {
            menuGroup.getMenus().remove(this);
        }

        menuGroup.getMenus().add(this);
        this.menuGroup = menuGroup;
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
