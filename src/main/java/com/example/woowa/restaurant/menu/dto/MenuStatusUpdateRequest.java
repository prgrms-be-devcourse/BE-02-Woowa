package com.example.woowa.restaurant.menu.dto;

import com.example.woowa.restaurant.menu.enums.MenuStatus;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuStatusUpdateRequest {

    @NotNull(message = "메뉴 상태는 필수입니다.")
    MenuStatus menuStatus;

}
