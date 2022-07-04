package com.example.woowa.admin.converter;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.entity.Admin;
import java.util.regex.Pattern;

public class AdminConverter {

  public static Admin toAdmin(AdminCreateRequest adminCreateRequest) {
    return new Admin(adminCreateRequest.getLoginId(), adminCreateRequest.getLoginPassword());
  }

  public static AdminFindResponse toAdminDto(Admin admin) {
    return new AdminFindResponse(admin.getLoginId());
  }
}