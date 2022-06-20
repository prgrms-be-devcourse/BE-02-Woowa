package com.example.woowa.admin.service.impl;

import com.example.woowa.admin.repository.AdminRepository;
import com.example.woowa.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

}
