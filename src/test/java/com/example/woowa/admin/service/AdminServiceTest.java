package com.example.woowa.admin.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.dto.AdminUpdateRequest;
import com.example.woowa.admin.entity.Admin;
import com.example.woowa.admin.repository.AdminRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class AdminServiceTest {
  @Autowired
  private AdminService adminService;

  @MockBean
  private AdminRepository adminRepository;

  @Test
  @DisplayName("관리자 생성")
  void createAdmin() {
    given(adminRepository.save(any())).willReturn(new Admin("dev12", "Programmers12!"));

    AdminCreateRequest adminCreateRequest = new AdminCreateRequest("dev12", "Programmers12!");
    AdminFindResponse adminFindResponse = adminService.createAdmin(adminCreateRequest);

    Assertions.assertThat(adminFindResponse.getLoginId()).isEqualTo("dev12");
  }

  @Test
  @DisplayName("관리자 조회")
  void findAdmin() {
    given(adminRepository.findByLoginId(anyString())).willReturn(
        Optional.of(new Admin("dev12", "Programmers12!"))
    );

    AdminFindResponse adminFindResponse = adminService.findAdmin("dev12");

    Assertions.assertThat(adminFindResponse.getLoginId()).isEqualTo("dev12");
  }

  @Test
  @DisplayName("관리자 업데이트")
  void updateAdmin() {
    given(adminRepository.findByLoginId(anyString())).willReturn(
        Optional.of(new Admin("dev12", "Programmers12!"))
    );
    AdminUpdateRequest adminUpdateRequest = new AdminUpdateRequest("Programmers123$");

    AdminFindResponse adminFindResponse = adminService.updateAdmin("dev12", adminUpdateRequest);

    Assertions.assertThat(adminFindResponse.getLoginId()).isEqualTo("dev12");
  }

  @Test
  @DisplayName("관리자 삭제")
  void deleteAdmin() {
    Admin admin = new Admin("dev12", "Programmers12!");
    given(adminRepository.findByLoginId(anyString())).willReturn(
        Optional.of(admin)
    );

    adminService.deleteAdmin("dev12");

    verify(adminRepository).delete(admin);
  }

}