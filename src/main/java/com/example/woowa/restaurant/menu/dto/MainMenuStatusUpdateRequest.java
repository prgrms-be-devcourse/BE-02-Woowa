package com.example.woowa.restaurant.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MainMenuStatusUpdateRequest {

    @NotNull(message = "메인 메뉴 설정 여부를 입력해주세요")
    private final Boolean isMainMenu;

    public MainMenuStatusUpdateRequest(@JsonProperty("isMainMenu") Boolean isMainMenu) {
        this.isMainMenu = isMainMenu;
    }

}
