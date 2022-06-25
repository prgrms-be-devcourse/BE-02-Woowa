package com.example.woowa.restaurant.menu.entity;

import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
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
import org.springframework.util.StringUtils;

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

    @Column(length = 500)
    private String description;

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isMain;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MenuStatus menuStatus;

    private Menu(MenuGroup menuGroup, String title, Integer price, String description,
            Boolean isMain,
            MenuStatus menuStatus) {
        this.menuGroup = menuGroup;
        this.title = title;
        this.price = price;
        this.description = description;
        this.isMain = isMain;
        this.menuStatus = menuStatus;
    }

    public static Menu createMenu(MenuGroup menuGroup, String title, Integer price,
            String description,
            Boolean isMain,
            MenuStatus menuStatus) {
        Menu menu = new Menu(menuGroup, title, price, convertToNullIfEmptyDescription(description),
                isMain,
                menuStatus);
        menuGroup.addMenu(menu);
        return menu;
    }

    public void update(String title, Integer price, String description) {
        this.title = title;
        this.price = price;
        this.description = convertToNullIfEmptyDescription(description);
    }

    public void changeMenuStatus(MenuStatus menuStatus) {
        this.menuStatus = menuStatus;
    }

    public void setMainMenu() {
        isMain = true;
    }

    public void cancelMainMenu() {
        isMain = false;
    }

    private static String convertToNullIfEmptyDescription(String description) {
        return StringUtils.hasText(description) ? description : null;
    }
}
