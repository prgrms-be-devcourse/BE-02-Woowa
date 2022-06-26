package com.example.woowa.admin.converter;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.entity.Admin;
import java.util.regex.Pattern;

public class AdminConverter {

  public static Admin toAdmin(AdminCreateRequest adminCreateRequest) {
    validateAdmin(adminCreateRequest.getLoginId(), adminCreateRequest.getLoginPassword());
    return new Admin(adminCreateRequest.getLoginId(), adminCreateRequest.getLoginPassword());
  }

  private static void validateAdmin(String loginId, String loginPassword) {
    assert Pattern.matches("^(?=.*\\d)(?=.*[a-zA-Z])[a-zA-z0-9]{5,10}$", loginId);
    assert Pattern.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!,@,#,$,%]).{8,}$", loginPassword);
  }

  public static AdminFindResponse toAdminDto(Admin admin) {
    return new AdminFindResponse(admin.getLoginId());
  }
}