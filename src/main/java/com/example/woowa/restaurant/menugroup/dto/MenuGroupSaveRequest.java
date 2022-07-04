package com.example.woowa.restaurant.menugroup.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@Getter
public class MenuGroupSaveRequest {

    @NotBlank
    @Length(max = 100)
    private final String title;

    @Length(max = 500)
    private final String description;
}
