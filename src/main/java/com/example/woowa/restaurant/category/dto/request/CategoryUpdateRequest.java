package com.example.woowa.restaurant.category.dto.request;


import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {

    @Size(min = 1, max = 10, message = "카테고리명은 1자이상 10자이하여야 합니다.")
    private String name;

}
