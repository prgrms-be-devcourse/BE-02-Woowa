package com.example.woowa.admin.service.impl;

import com.example.woowa.admin.repository.AdminRepository;
import com.example.woowa.admin.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;

    public AdminServiceImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }


}
