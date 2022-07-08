package com.example.woowa.restaurant.menugroup.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class MenuGroupUpdateRequest {

    @NotBlank(message = "메뉴그룹명은 null이거나 빈 문자열일 수 없습니다.")
    @Length(max = 100, message = "메뉴그룹명은 최대 100자 입니다.")
    private final String title;

    @Length(max = 500, message = "메뉴그룹 설명은 최대 500자 입니다.")
    private final String description;
}
