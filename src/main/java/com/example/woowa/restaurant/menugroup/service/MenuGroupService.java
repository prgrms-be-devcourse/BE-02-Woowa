package com.example.woowa.restaurant.menugroup.service;

import com.example.woowa.restaurant.menugroup.repository.MenuGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

}
