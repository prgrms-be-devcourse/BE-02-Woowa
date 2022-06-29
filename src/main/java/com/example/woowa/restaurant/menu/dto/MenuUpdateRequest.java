package com.example.woowa.restaurant.menu.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Getter
public class MenuUpdateRequest {

    @NotBlank(message = "메뉴명은 필수입니다.")
    @Length(max = 100, message = "메뉴명은 최대 100자까지 입력할 수 있습니다.")
    private String title;

    @Length(max = 1000, message = "메뉴 설명은 최대 1000자까지 입력할 수 있습니다.")
    private String description;

    @Positive(message = "메뉴 가격은 0보다 큰 값이어야 합니다.")
    private Integer price;
}
