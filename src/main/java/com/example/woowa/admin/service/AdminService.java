package com.example.woowa.admin.service;

import com.example.woowa.admin.converter.AdminMapper;
import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.dto.AdminUpdateRequest;
import com.example.woowa.admin.entity.Admin;
import com.example.woowa.admin.repository.AdminRepository;
import com.example.woowa.restaurant.restaurant.entity.Restaurant;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class AdminService {

    private final RestaurantService restaurantService;
    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    @Transactional
    public AdminFindResponse createAdmin(AdminCreateRequest adminCreateRequest) {
        Admin admin = adminMapper.toAdmin(adminCreateRequest);
        admin = adminRepository.save(admin);
        return adminMapper.toAdminDto(admin);
    }

    public AdminFindResponse findAdmin(String loginId) {
        Admin admin = findAdminEntity(loginId);
        return adminMapper.toAdminDto(admin);
    }

    @Transactional
    public AdminFindResponse updateAdmin(String loginId, AdminUpdateRequest adminUpdateRequest) {
        Admin admin = findAdminEntity(loginId);
        admin.changePassword(adminUpdateRequest.getLoginPassword());
        return adminMapper.toAdminDto(admin);
    }

    @Transactional
    public void deleteAdmin(String loginId) {
        Admin admin = findAdminEntity(loginId);
        adminRepository.delete(admin);
    }

    private Admin findAdminEntity(String loginId) {
        return adminRepository.findByLoginId(loginId).orElseThrow(()-> new RuntimeException("admin not existed"));
    }

    @Transactional
    public void permitRestaurant(Long restaurantId) {
        Restaurant restaurant = restaurantService.findRestaurantEntityById(restaurantId);
        restaurant.setPermitted();
    }

}
