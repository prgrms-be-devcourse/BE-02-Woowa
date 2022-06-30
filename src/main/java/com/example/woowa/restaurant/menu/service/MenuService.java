package com.example.woowa.restaurant.menu.service;

import com.example.woowa.restaurant.menu.MenuMapper;
import com.example.woowa.restaurant.menu.dto.MainMenuStatusUpdateRequest;
import com.example.woowa.restaurant.menu.dto.MenuResponse;
import com.example.woowa.restaurant.menu.dto.MenuSaveRequest;
import com.example.woowa.restaurant.menu.dto.MenuStatusUpdateRequest;
import com.example.woowa.restaurant.menu.dto.MenuUpdateRequest;
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
    public Long addMenu(MenuSaveRequest request) {
        MenuGroup findMenuGroup = menuGroupService.findMenuGroupEntityById(
                request.getMenuGroupId());
        Menu menu = Menu.createMenu(findMenuGroup, request.getTitle(), request.getPrice(),
                request.getDescription(), false,
                MenuStatus.SALE);
        return menuRepository.save(menu).getId();
    }

    public MenuResponse findMenuById(Long menuId) {
        return MenuMapper.INSTANCE.toMenuResponse(findMenuEntityById(menuId));
    }

    @Transactional
    public void updateMenu(Long menuId, MenuUpdateRequest request) {
        findMenuEntityById(menuId).update(request.getTitle(), request.getPrice(),
                request.getDescription());
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu findMenu = findMenuEntityById(menuId);
        menuRepository.delete(findMenu);
    }

    @Transactional
    public void changeMainMenuStatus(Long menuId, MainMenuStatusUpdateRequest request) {
        if (request.getIsMainMenu()) {
            findMenuEntityById(menuId).setMainMenu();
        } else {
            findMenuEntityById(menuId).cancelMainMenu();
        }
    }

    @Transactional
    public void changeMenuStatus(Long menuId, MenuStatusUpdateRequest request) {
        findMenuEntityById(menuId).changeMenuStatus(request.getMenuStatus());
    }

    public Menu findMenuEntityById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 menuId 입니다."));
    }
}
