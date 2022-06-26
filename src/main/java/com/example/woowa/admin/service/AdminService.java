package com.example.woowa.admin.service;

import com.example.woowa.admin.converter.AdminConverter;
import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.dto.AdminUpdateRequest;
import com.example.woowa.admin.entity.Admin;
import com.example.woowa.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminService {

    private final AdminRepository adminRepository;

    @Transactional
    public AdminFindResponse createAdmin(AdminCreateRequest adminCreateRequest) {
        Admin admin = AdminConverter.toAdmin(adminCreateRequest);
        admin = adminRepository.save(admin);
        return AdminConverter.toAdminDto(admin);
    }

    public AdminFindResponse findAdmin(String loginId) {
        Admin admin = findAdminEntity(loginId);
        return AdminConverter.toAdminDto(admin);
    }

    @Transactional
    public AdminFindResponse updateAdmin(String loginId, AdminUpdateRequest adminUpdateRequest) {
        Admin admin = findAdminEntity(loginId);
        admin.changePassword(adminUpdateRequest.getLoginPassword());
        return AdminConverter.toAdminDto(admin);
    }

    @Transactional
    public void deleteAdmin(String loginId) {
        Admin admin = findAdminEntity(loginId);
        adminRepository.delete(admin);
    }

    private Admin findAdminEntity(String loginId) {
        return adminRepository.findByLoginId(loginId).orElseThrow(()-> new RuntimeException("admin not existed"));
    }
}
