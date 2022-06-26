package com.example.woowa.admin.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.dto.AdminUpdateRequest;
import com.example.woowa.admin.repository.AdminRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminServiceTest {
  @Autowired
  private AdminService adminService;

  @Autowired
  private AdminRepository adminRepository;

  public String getAdminId() {
    AdminCreateRequest  adminCreateRequest = new AdminCreateRequest("dev12", "Programmers12!");
    return adminService.createAdmin(adminCreateRequest).getLoginId();
  }

  @AfterEach
  void afterTest() {
    adminRepository.deleteAll();
  }

  @Test
  @DisplayName("관리자 생성")
  void createAdmin() {
    AdminCreateRequest  adminCreateRequest = new AdminCreateRequest("dev12", "Programmers12!");
    adminService.createAdmin(adminCreateRequest).getLoginId();

    assertThat(adminRepository.count(), is(1l));
  }

  @Test
  @DisplayName("관리자 조회")
  void findAdmin() {
    String loginId = getAdminId();

    AdminFindResponse adminFindResponse = adminService.findAdmin(loginId);

    assertThat(adminFindResponse.getLoginId(), is("dev12"));
  }

  @Test
  @DisplayName("관리자 업데이트")
  void updateAdmin() {
    String loginId = getAdminId();
    AdminUpdateRequest adminUpdateRequest = new AdminUpdateRequest("Programmers123$");

    adminService.updateAdmin(loginId, adminUpdateRequest);

    assertThat(adminRepository.count(), is(1l));
  }

  @Test
  @DisplayName("관리자 삭제")
  void deleteAdmin() {
    String loginId = getAdminId();

    adminService.deleteAdmin(loginId);

    assertThat(adminRepository.count(), is(0l));
  }

}