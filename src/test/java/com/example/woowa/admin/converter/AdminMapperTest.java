package com.example.woowa.admin.converter;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.entity.Admin;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminMapperTest {
  @Autowired
  private AdminMapper adminMapper;

  @Test
  @DisplayName("관리자 엔티티 변환")
  void toAdmin() {
    AdminCreateRequest adminCreateRequest = new AdminCreateRequest("dev12", "Programmers12!");

    Admin admin = adminMapper.toAdmin(adminCreateRequest);

    Assertions.assertThat(admin.getLoginId()).isEqualTo("dev12");
    Assertions.assertThat(admin.getPassword()).isEqualTo("Programmers12!");
    Assertions.assertThat(admin.getPhoneNumber()).isEqualTo("");
  }

  @Test
  @DisplayName("관리자 dto 변환")
  void toAdminDto() {
    Admin admin = new Admin("dev12", "Programmers12!");

    AdminFindResponse adminFindResponse = adminMapper.toAdminDto(admin);

    Assertions.assertThat(adminFindResponse.getLoginId()).isEqualTo("dev12");
  }
}