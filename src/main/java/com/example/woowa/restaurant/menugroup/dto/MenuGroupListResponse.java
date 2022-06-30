package com.example.woowa.restaurant.menugroup.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuGroupListResponse {

    private List<MenuGroupResponse> menuGroups;
}
