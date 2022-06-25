package com.example.woowa.restaurant.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.example.woowa.restaurant.menu.entity.Menu;
import com.example.woowa.restaurant.menu.enums.MenuStatus;
import com.example.woowa.restaurant.menu.repository.MenuRepository;
import com.example.woowa.restaurant.menugroup.entity.MenuGroup;
import com.example.woowa.restaurant.menugroup.service.MenuGroupService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuGroupService menuGroupService;

    @Mock
    MenuGroup menuGroup;

    MenuService menuService;

    Menu menu;

    @BeforeEach
    void init() {
        menuService = new MenuService(menuRepository, menuGroupService);
        menu = Menu.createMenu(menuGroup, "김치볶음밥", 8000, "맛있어요", false, MenuStatus.SALE);
    }

    @Test
    @DisplayName("메뉴를 저장한다.")
    void addMenuTest() {
        // Given
        Long menuGroupId = 1L;
        String title = "김치볶음밥";
        String description = "맛있어요";
        Integer price = 8000;

        given(menuRepository.save(any())).willReturn(menu);
        given(menuGroupService.findMenuGroupById(menuGroupId)).willReturn(menuGroup);

        // When
        menuService.addMenu(menuGroupId, title, description, price);

        // Then
        then(menuRepository).should().save(any());
    }

    @Test
    @DisplayName("메뉴를 단건 조회한다.")
    void findMenuByIdTest() {
        // Given
        Long menuId = 1L;
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        // When
        Menu findMenu = menuService.findMenuById(menuId);

        // Then
        assertThat(findMenu).usingRecursiveComparison().isEqualTo(menu);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 단건 조회하려 하면 예외가 발생한다.")
    void findMenuByNotExistsIdTest() {
        // Given
        Long menuId = 1L;
        given(menuRepository.findById(menuId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> menuService.findMenuById(menuId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 정보를 업데이트 한다.")
    void updateMenuTest() {
        // Given
        Long menuId = 1L;
        String newTitle = "참치김밥";
        String newDescription = "맛있는 참치김밥";
        Integer newPrice = 4000;
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        // When
        menuService.updateMenu(menuId, newTitle, newDescription, newPrice);

        // Then
        Menu findMenu = menuService.findMenuById(menuId);
        assertThat(findMenu.getTitle()).isEqualTo(newTitle);
        assertThat(findMenu.getDescription()).isEqualTo(newDescription);
        assertThat(findMenu.getPrice()).isEqualTo(newPrice);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴의 정보를 업데이트하려 하면 예외가 발생한다.")
    void updateMenuNotExistsIdTest() {
        // Given
        Long wrongMenuId = -1L;
        String newTitle = "참치김밥";
        String newDescription = "맛있는 참치김밥";
        Integer newPrice = 4000;
        given(menuRepository.findById(wrongMenuId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(
                () -> menuService.updateMenu(wrongMenuId, newTitle, newDescription, newPrice))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("입력받은 ID의 메뉴를 삭제한다.")
    void deleteMenuTest() {
        // Given
        Long menuId = 1L;
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        // When
        menuService.deleteMenu(menuId);

        // Then
        then(menuRepository).should().delete(menu);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 삭제하려 하면 예외가 발생한다.")
    void deleteMenuNotExistsIdTest() {
        // Given
        Long wrongMenuId = -1L;
        given(menuRepository.findById(wrongMenuId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> menuService.deleteMenu(wrongMenuId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("입력받은 ID의 메뉴를 대표메뉴로 등록한다.")
    void setMainMenuTest() {
        // Given
        Long menuId = 1L;
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        // When
        menuService.setMainMenu(menuId);

        // Then
        Menu findMenu = menuService.findMenuById(menuId);
        assertThat(findMenu.getIsMain()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 대표메뉴로 등록하려 하면 예외가 발생한다.")
    void setMainMenuNotExistsIdTest() {
        // Given
        Long wrongMenuId = 1L;
        given(menuRepository.findById(wrongMenuId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> menuService.setMainMenu(wrongMenuId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("입력받은 ID의 메뉴를 대표메뉴에서 등록 해제한다.")
    void cancelMainMenuTest() {
        // Given
        Long menuId = 1L;
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        // When
        menuService.cancelMainMenu(menuId);

        // Then
        Menu findMenu = menuService.findMenuById(menuId);
        assertThat(findMenu.getIsMain()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 대표메뉴에서 등록 해제하려 하면 예외가 발생한다.")
    void cancelMainMenuNotExistsIdTest() {
        // Given
        Long wrongMenuId = 1L;
        given(menuRepository.findById(wrongMenuId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> menuService.cancelMainMenu(wrongMenuId))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 상태를 판매중으로 변경한다.")
    void changeMenuStatusToSaleTest() {
        // Given
        Long menuId = 1L;
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        // When
        menuService.changeMenuStatus(menuId, MenuStatus.SALE);

        // Then
        Menu findMenu = menuService.findMenuById(menuId);
        assertThat(findMenu.getMenuStatus()).isEqualTo(MenuStatus.SALE);
    }

    @Test
    @DisplayName("메뉴 상태를 숨김으로 변경한다.")
    void changeMenuStatusToHiddenTest() {
        // Given
        Long menuId = 1L;
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        // When
        menuService.changeMenuStatus(menuId, MenuStatus.HIDDEN);

        // Then
        Menu findMenu = menuService.findMenuById(menuId);
        assertThat(findMenu.getMenuStatus()).isEqualTo(MenuStatus.HIDDEN);
    }

    @Test
    @DisplayName("메뉴 상태를 품절로 변경한다.")
    void changeMenuStatusToSoldOutTest() {
        // Given
        Long menuId = 1L;
        given(menuRepository.findById(menuId)).willReturn(Optional.of(menu));

        // When
        menuService.changeMenuStatus(menuId, MenuStatus.SOLD_OUT);

        // Then
        Menu findMenu = menuService.findMenuById(menuId);
        assertThat(findMenu.getMenuStatus()).isEqualTo(MenuStatus.SOLD_OUT);
    }

    @Test
    @DisplayName("존재하지 않는 메뉴의 상태를 변경하려 하면 예외가 발생한다.")
    void changeMenuStatusNotExistsIdTest() {
        // Given
        Long wrongMenuId = -1L;
        given(menuRepository.findById(wrongMenuId)).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> menuService.changeMenuStatus(wrongMenuId, MenuStatus.SALE))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}