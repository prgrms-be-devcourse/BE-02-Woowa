package com.example.woowa.admin.converter;

import static org.junit.jupiter.api.Assertions.*;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.entity.Admin;
import com.example.woowa.admin.repository.AdminRepository;
import com.example.woowa.admin.service.AdminService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AdminMapperTest {
  @Autowired
  private AdminMapper adminMapper;

  @Test
  void toAdmin() {
    AdminCreateRequest adminCreateRequest = new AdminCreateRequest("dev12", "Programmers12!");

    Admin admin = adminMapper.toAdmin(adminCreateRequest);

    Assertions.assertThat(admin.getLoginId()).isEqualTo("dev12");
    Assertions.assertThat(admin.getPassword()).isEqualTo("Programmers12!");
    Assertions.assertThat(admin.getPhoneNumber()).isEqualTo("");
  }

  @Test
  void toAdminDto() {
    Admin admin = new Admin("dev12", "Programmers12!");

    AdminFindResponse adminFindResponse = adminMapper.toAdminDto(admin);

    Assertions.assertThat(adminFindResponse.getLoginId()).isEqualTo("dev12");
  }
}