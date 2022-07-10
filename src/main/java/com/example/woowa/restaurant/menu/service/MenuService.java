package com.example.woowa.restaurant.menu.service;

import com.example.woowa.common.exception.ErrorMessage;
import com.example.woowa.common.exception.NotFoundException;
import com.example.woowa.restaurant.menu.dto.MainMenuStatusUpdateRequest;
import com.example.woowa.restaurant.menu.dto.MenuResponse;
import com.example.woowa.restaurant.menu.dto.MenuSaveRequest;
import com.example.woowa.restaurant.menu.dto.MenuStatusUpdateRequest;
import com.example.woowa.restaurant.menu.dto.MenuUpdateRequest;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.mapper.MenuMapper;
import com.example.woowa.restaurant.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    @Transactional
    public Long addMenu(MenuSaveRequest request) {
        return menuRepository.save(menuMapper.toMenu(request)).getId();
    }

    public MenuResponse findMenuById(Long menuId) {
        return menuMapper.toMenuResponse(findMenuEntityById(menuId));
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
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_MENU.getMessage()));
    }
}
