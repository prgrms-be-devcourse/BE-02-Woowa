package com.example.woowa.restaurant.menu.dto;

import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MenuStatusUpdateRequest {

    @NotNull(message = "메뉴 상태는 필수입니다.")
    private final MenuStatus menuStatus;

    public MenuStatusUpdateRequest(@JsonProperty("menuStatus") String menuStatus) {
        this.menuStatus = MenuStatus.find(menuStatus);
    }
}
