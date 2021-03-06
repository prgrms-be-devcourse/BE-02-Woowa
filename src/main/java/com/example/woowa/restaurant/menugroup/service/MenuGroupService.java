package com.example.woowa.restaurant.menugroup.service;

import com.example.woowa.common.exception.ErrorMessage;
import com.example.woowa.common.exception.NotFoundException;
import com.example.woowa.restaurant.menugroup.mapper.MenuGroupMapper;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupListResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupSaveRequest;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupUpdateRequest;
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
    private final MenuGroupMapper mapper;

    public MenuGroup findMenuGroupEntityById(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_MENU_GROUP.getMessage()));
    }

    public MenuGroupResponse findMenuById(Long menuGroupId) {
        return mapper.toMenuGroupResponse(findMenuGroupEntityById(menuGroupId));
    }

    public MenuGroupListResponse findMenuGroupByRestaurant(Long restaurantId) {
        Restaurant findRestaurant = restaurantService.findRestaurantEntityById(restaurantId);
        return mapper.toMenuGroupListResponse(menuGroupRepository.findByRestaurant(findRestaurant));
    }

    @Transactional
    public Long addMenuGroup(Long restaurantId, MenuGroupSaveRequest request) {
        Restaurant findRestaurant = restaurantService.findRestaurantEntityById(restaurantId);
        MenuGroup menuGroup = MenuGroup.createMenuGroup(findRestaurant, request.getTitle(),
                request.getDescription());
        return menuGroupRepository.save(menuGroup).getId();
    }

    @Transactional
    public void updateMenuGroup(Long menuGroupId, MenuGroupUpdateRequest request) {
        findMenuGroupEntityById(menuGroupId).update(request.getTitle(), request.getDescription());
    }

    @Transactional
    public void deleteMenuGroup(Long menuGroupId) {
        MenuGroup findMenuGroup = findMenuGroupEntityById(menuGroupId);
        menuGroupRepository.delete(findMenuGroup);
    }
}
