package com.example.woowa.restaurant.category.mapper;

import com.example.woowa.restaurant.category.dto.response.CategoryCreateResponse;
import com.example.woowa.restaurant.category.dto.response.CategoryFindResponse;
import com.example.woowa.restaurant.category.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CategoryMapper {

    CategoryCreateResponse toCreateResponseDto(Category category);

    CategoryFindResponse toFindResponseDto(Category category);

}
