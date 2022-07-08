package com.example.woowa.restaurant.menugroup.mapper;

import com.example.woowa.restaurant.menugroup.dto.MenuGroupListResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupResponse;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MenuGroupMapper {

    MenuGroupResponse toMenuGroupResponse(MenuGroup menuGroup);

    List<MenuGroupResponse> toMenuGroupResponseList(List<MenuGroup> menuGroups);

    default MenuGroupListResponse toMenuGroupListResponse(List<MenuGroup> menuGroups) {
        return new MenuGroupListResponse(toMenuGroupResponseList(menuGroups));
    }
}
