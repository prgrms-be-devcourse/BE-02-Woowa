package com.example.woowa.admin.converter;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.entity.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
  Admin toAdmin(AdminCreateRequest adminCreateRequest);
  AdminFindResponse toAdminDto(Admin admin);
}
