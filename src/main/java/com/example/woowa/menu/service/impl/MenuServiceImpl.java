package com.example.woowa.menu.service.impl;

import com.example.woowa.menu.repository.MenuRepository;
import com.example.woowa.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
}
