package com.example.woowa.category.service.impl;

import com.example.woowa.category.repository.CategoryRepository;
import com.example.woowa.category.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(
        CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

}
