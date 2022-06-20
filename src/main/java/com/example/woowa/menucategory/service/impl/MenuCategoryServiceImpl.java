package com.example.woowa.menucategory.service.impl;

import com.example.woowa.menucategory.repository.MenuCategoryRepository;
import com.example.woowa.menucategory.service.MenuCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuCategoryServiceImpl implements MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;

}
