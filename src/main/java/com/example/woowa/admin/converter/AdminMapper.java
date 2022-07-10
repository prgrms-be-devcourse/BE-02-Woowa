package com.example.woowa.admin.converter;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.entity.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL, componentModel = "spring")
public interface AdminMapper {
  Admin toAdmin(AdminCreateRequest adminCreateRequest);
  AdminFindResponse toAdminDto(Admin admin);
}
