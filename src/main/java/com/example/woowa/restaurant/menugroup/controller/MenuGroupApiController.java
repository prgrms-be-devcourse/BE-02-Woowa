package com.example.woowa.restaurant.menugroup.controller;

import com.example.woowa.restaurant.menugroup.dto.MenuGroupListResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupResponse;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupSaveRequest;
import com.example.woowa.restaurant.menugroup.dto.MenuGroupUpdateRequest;
import com.example.woowa.restaurant.menugroup.service.MenuGroupService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MenuGroupApiController {

    private final MenuGroupService menuGroupService;

    @PostMapping("/api/v1/restaurant/{restaurantId}/menu-groups")
    public ResponseEntity<Void> addMenuGroup(@PathVariable Long restaurantId,
            @RequestBody @Validated
            MenuGroupSaveRequest request) {
        Long menuGroupId = menuGroupService.addMenuGroup(restaurantId, request);
        return ResponseEntity.created(URI.create("/api/v1/menu-groups/" + menuGroupId))
                .build();
    }

    @GetMapping("/api/v1/menu-groups/{menuGroupId}")
    public ResponseEntity<MenuGroupResponse> findMenuGroup(@PathVariable Long menuGroupId) {
        return ResponseEntity.ok(menuGroupService.findMenuById(menuGroupId));
    }

    @GetMapping("/api/v1/restaurant/{restaurantId}/menu-groups")
    public ResponseEntity<MenuGroupListResponse> findMenuGroupList(
            @PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuGroupService.findMenuGroupByRestaurant(restaurantId));
    }

    @PatchMapping("/api/v1/menu-groups/{menuGroupId}")
    public ResponseEntity<Void> updateMenuGroup(@PathVariable Long menuGroupId,
            @RequestBody @Validated
            MenuGroupUpdateRequest request) {
        menuGroupService.updateMenuGroup(menuGroupId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/menu-groups/{menuGroupId}")
    public ResponseEntity<Void> deleteMenuGroup(@PathVariable Long menuGroupId) {
        menuGroupService.deleteMenuGroup(menuGroupId);
        return ResponseEntity.ok().build();
    }
}
