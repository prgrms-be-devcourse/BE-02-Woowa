package com.example.woowa.admin.controller;

import com.example.woowa.admin.dto.AdminCreateRequest;
import com.example.woowa.admin.dto.AdminFindResponse;
import com.example.woowa.admin.dto.AdminUpdateRequest;
import com.example.woowa.admin.service.AdminService;
import com.example.woowa.restaurant.restaurant.service.RestaurantService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdminController {
  private final AdminService adminService;

  @PostMapping
  public AdminFindResponse createAdmin(@RequestBody @Valid AdminCreateRequest adminCreateRequest) {
    return adminService.createAdmin(adminCreateRequest);
  }

  @GetMapping("/{loginId}")
  public AdminFindResponse findAdmin(@PathVariable String loginId) {
    return adminService.findAdmin(loginId);
  }

  @PutMapping("/{loginId}")
  public AdminFindResponse updateAdmin(@PathVariable String loginId, @RequestBody @Valid
      AdminUpdateRequest adminUpdateRequest) {
    return adminService.updateAdmin(loginId, adminUpdateRequest);
  }

  @DeleteMapping("/{loginId}")
  public String deleteAdmin(@PathVariable String loginId) {
    adminService.deleteAdmin(loginId);
    return "delete id - " + loginId;
  }

  @PatchMapping("/permit/restaurants/{restaurantId}")
  public String permitRestaurant(@PathVariable Long restaurantId) {
    adminService.permitRestaurant(restaurantId);
    return "restaurant id(" + restaurantId + ") permitted.";
  }

}
