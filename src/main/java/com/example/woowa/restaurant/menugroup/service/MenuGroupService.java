package com.example.woowa.restaurant.menugroup.service;

import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.repository.MenuGroupRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;
    private final RestaurantService restaurantService;

    public MenuGroup findMenuGroupById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 menuGroupId 입니다."));
    }

    @Transactional
    public Long addMenuGroup(Long restaurantId, String title, String description) {
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);
        MenuGroup menuGroup = MenuGroup.createMenuGroup(findRestaurant, title, description);
        return menuGroupRepository.save(menuGroup).getId();
    }

    @Transactional
    public void updateMenuGroup(Long menuGroupId, String title, String description) {
        findMenuGroupById(menuGroupId).update(title, description);
    }

    @Transactional
    public void deleteMenuGroup(Long menuGroupId) {
        MenuGroup findMenuGroup = findMenuGroupById(menuGroupId);
        menuGroupRepository.delete(findMenuGroup);
    }
}
