package com.example.woowa.category.service.impl;

import com.example.woowa.category.repository.CategoryRepository;
import com.example.woowa.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

}
