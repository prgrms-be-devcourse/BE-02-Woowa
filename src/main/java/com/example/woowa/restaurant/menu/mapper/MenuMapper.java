package com.example.woowa.restaurant.menu.mapper;

import com.example.woowa.restaurant.menu.dto.MenuResponse;
import com.example.woowa.restaurant.menu.dto.MenuSaveRequest;
import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menugroup.service.MenuGroupService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {MenuGroupService.class})
public interface MenuMapper {

    MenuResponse toMenuResponse(Menu menu);

    @Mappings({
            @Mapping(target = "menuGroup", source = "menuGroupId"),
            @Mapping(target = "isMain", constant = "false"),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "menuStatus", expression = "java(com.example.woowa.restaurant.menu.enums.MenuStatus.SALE)")
    })
    Menu toMenu(MenuSaveRequest request);

}
