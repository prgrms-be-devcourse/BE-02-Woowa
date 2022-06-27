package com.example.woowa.restaurant.menu.service;

import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menu.repository.MenuRepository;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.service.MenuGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;

    @Transactional
    public Long addMenu(Long menuGroupId, String title, String description, Integer price) {
        MenuGroup findMenuGroup = menuGroupService.findMenuGroupById(menuGroupId);
        Menu menu = Menu.createMenu(findMenuGroup, title, price, description, false,
                MenuStatus.SALE);
        return menuRepository.save(menu).getId();
    }

    public Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 menuId 입니다."));
    }

    @Transactional
    public void updateMenu(Long menuId, String title, String description, Integer price) {
        findMenuById(menuId).update(title, price, description);
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu findMenu = findMenuById(menuId);
        menuRepository.delete(findMenu);
    }

    @Transactional
    public void setMainMenu(Long menuId) {
        findMenuById(menuId).setMainMenu();
    }

    @Transactional
    public void cancelMainMenu(Long menuId) {
        findMenuById(menuId).cancelMainMenu();
    }

    @Transactional
    public void changeMenuStatus(Long menuId, MenuStatus menuStatus) {
        findMenuById(menuId).changeMenuStatus(menuStatus);
    }
}
