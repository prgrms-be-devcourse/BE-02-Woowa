package com.example.woowa.restaurant.menu.controller;

import com.example.woowa.restaurant.menu.dto.MenuResponse;
import com.example.woowa.restaurant.menu.dto.MenuSaveRequest;
import com.example.woowa.restaurant.menu.dto.MainMenuStatusUpdateRequest;
import com.example.woowa.restaurant.menu.dto.MenuStatusUpdateRequest;
import com.example.woowa.restaurant.menu.dto.MenuUpdateRequest;
import com.example.woowa.restaurant.menu.service.MenuService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menus")
public class MenuApiController {

    private final MenuService menuService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addMenu(@RequestBody @Validated MenuSaveRequest menuSaveRequest) {
        Long menuId = menuService.addMenu(menuSaveRequest);
        return ResponseEntity.created(URI.create("/api/v1/menus/" + menuId)).build();
    }

    @GetMapping(value = "/{menuId}")
    public ResponseEntity<MenuResponse> findDetailMenu(@PathVariable Long menuId) {
        return ResponseEntity.ok().body(menuService.findMenuById(menuId));
    }

    @PatchMapping(value = "/{menuId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateMenu(@PathVariable Long menuId, @RequestBody @Validated
    MenuUpdateRequest request) {
        menuService.updateMenu(menuId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{menuId}/main-menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeMainMenuStatus(@PathVariable Long menuId,
            @RequestBody MainMenuStatusUpdateRequest request) {
        System.out.println(request.getIsMainMenu());
        menuService.changeMainMenuStatus(menuId, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/{menuId}/menu-status", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> changeMenuStatus(@PathVariable Long menuId, @RequestBody @Validated
    MenuStatusUpdateRequest request) {
        menuService.changeMenuStatus(menuId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{menuId}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long menuId) {
        menuService.deleteMenu(menuId);
        return ResponseEntity.ok().build();
    }
}
