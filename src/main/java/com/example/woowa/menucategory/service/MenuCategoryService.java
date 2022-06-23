package com.example.woowa.menucategory.service;

import com.example.woowa.menucategory.repository.MenuCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuCategoryService {

    private final MenuCategoryRepository menuCategoryRepository;

}
