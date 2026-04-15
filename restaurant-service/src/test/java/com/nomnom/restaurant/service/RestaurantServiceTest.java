package com.nomnom.restaurant.service;

import com.nomnom.restaurant.dto.CreateMenuItemRequest;
import com.nomnom.restaurant.dto.CreateRestaurantRequest;
import com.nomnom.restaurant.exception.ResourceNotFoundException;
import com.nomnom.restaurant.model.MenuItem;
import com.nomnom.restaurant.model.Restaurant;
import com.nomnom.restaurant.repository.MenuItemRepository;
import com.nomnom.restaurant.repository.RestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    // ── createRestaurant ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Creates restaurant with rating 0.0 and isOpen=true by default")
    void should_CreateRestaurant_With_DefaultRatingAndOpenStatus() {
        // Arrange
        final var request = new CreateRestaurantRequest();
        request.setName("Burger Palace");
        request.setCuisine("American");
        request.setAddress("123 Main St");

        given(restaurantRepository.save(any(Restaurant.class))).willAnswer(inv -> {
            final Restaurant r = inv.getArgument(0);
            r.setId(1L);
            return r;
        });

        // Act
        final var response = restaurantService.createRestaurant(request);

        // Assert
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Burger Palace");
        assertThat(response.getCuisine()).isEqualTo("American");
        assertThat(response.getRating()).isEqualTo(0.0);
        assertThat(response.getIsOpen()).isTrue();
        assertThat(response.getMenuItems()).isEmpty();
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    // ── getAllRestaurants ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Returns all restaurants with their menu items")
    void should_ReturnAllRestaurants_With_MenuItems() {
        // Arrange
        final var r1 = buildRestaurant(1L, "Burger Palace", true);
        final var r2 = buildRestaurant(2L, "Sushi World", true);
        final var item = buildMenuItem(10L, "Cheeseburger", 1L);

        given(restaurantRepository.findAll()).willReturn(List.of(r1, r2));
        given(menuItemRepository.findByRestaurantId(1L)).willReturn(List.of(item));
        given(menuItemRepository.findByRestaurantId(2L)).willReturn(List.of());

        // Act
        final var result = restaurantService.getAllRestaurants();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getMenuItems()).hasSize(1);
        assertThat(result.get(0).getMenuItems().get(0).getName()).isEqualTo("Cheeseburger");
        assertThat(result.get(1).getMenuItems()).isEmpty();
    }

    @Test
    @DisplayName("Returns empty list when no restaurants exist")
    void should_ReturnEmptyList_When_NoRestaurantsExist() {
        // Arrange
        given(restaurantRepository.findAll()).willReturn(List.of());

        // Act & Assert
        assertThat(restaurantService.getAllRestaurants()).isEmpty();
    }

    // ── getRestaurantWithMenu ─────────────────────────────────────────────────

    @Test
    @DisplayName("Returns restaurant with menu items when restaurant exists")
    void should_ReturnRestaurantWithMenu_When_RestaurantExists() {
        // Arrange
        final var restaurant = buildRestaurant(1L, "Burger Palace", true);
        final var item = buildMenuItem(10L, "Cheeseburger", 1L);

        given(restaurantRepository.findById(1L)).willReturn(Optional.of(restaurant));
        given(menuItemRepository.findByRestaurantId(1L)).willReturn(List.of(item));

        // Act
        final var response = restaurantService.getRestaurantWithMenu(1L);

        // Assert
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Burger Palace");
        assertThat(response.getMenuItems()).hasSize(1);
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when restaurant does not exist")
    void should_ThrowResourceNotFoundException_When_RestaurantDoesNotExist() {
        // Arrange
        given(restaurantRepository.findById(99L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> restaurantService.getRestaurantWithMenu(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── toggleOpenClosed ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Toggles isOpen from true to false")
    void should_ToggleOpen_From_TrueToFalse() {
        // Arrange
        final var restaurant = buildRestaurant(1L, "Burger Palace", true);
        given(restaurantRepository.findById(1L)).willReturn(Optional.of(restaurant));
        given(restaurantRepository.save(any(Restaurant.class))).willAnswer(inv -> inv.getArgument(0));
        given(menuItemRepository.findByRestaurantId(1L)).willReturn(List.of());

        // Act
        final var response = restaurantService.toggleOpenClosed(1L);

        // Assert
        assertThat(response.getIsOpen()).isFalse();
    }

    @Test
    @DisplayName("Toggles isOpen from false to true")
    void should_ToggleOpen_From_FalseToTrue() {
        // Arrange
        final var restaurant = buildRestaurant(1L, "Burger Palace", false);
        given(restaurantRepository.findById(1L)).willReturn(Optional.of(restaurant));
        given(restaurantRepository.save(any(Restaurant.class))).willAnswer(inv -> inv.getArgument(0));
        given(menuItemRepository.findByRestaurantId(1L)).willReturn(List.of());

        // Act
        final var response = restaurantService.toggleOpenClosed(1L);

        // Assert
        assertThat(response.getIsOpen()).isTrue();
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when toggling non-existent restaurant")
    void should_ThrowResourceNotFoundException_When_TogglingNonExistentRestaurant() {
        // Arrange
        given(restaurantRepository.findById(99L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> restaurantService.toggleOpenClosed(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── addMenuItem ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Adds menu item to existing restaurant with isAvailable=true")
    void should_AddMenuItem_When_RestaurantExists() {
        // Arrange
        final var restaurant = buildRestaurant(1L, "Burger Palace", true);
        final var request = new CreateMenuItemRequest();
        request.setName("Cheeseburger");
        request.setDescription("Classic cheeseburger");
        request.setPrice(new BigDecimal("9.99"));
        request.setCategory("Mains");

        given(restaurantRepository.findById(1L)).willReturn(Optional.of(restaurant));
        given(menuItemRepository.save(any(MenuItem.class))).willAnswer(inv -> {
            final MenuItem item = inv.getArgument(0);
            item.setId(10L);
            return item;
        });

        // Act
        final var response = restaurantService.addMenuItem(1L, request);

        // Assert
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("Cheeseburger");
        assertThat(response.getPrice()).isEqualByComparingTo("9.99");
        assertThat(response.getIsAvailable()).isTrue();
        assertThat(response.getRestaurantId()).isEqualTo(1L);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when adding item to non-existent restaurant")
    void should_ThrowResourceNotFoundException_When_AddingItemToNonExistentRestaurant() {
        // Arrange
        given(restaurantRepository.findById(99L)).willReturn(Optional.empty());
        final var request = new CreateMenuItemRequest();
        request.setName("Burger");
        request.setPrice(new BigDecimal("8.00"));

        // Act & Assert
        assertThatThrownBy(() -> restaurantService.addMenuItem(99L, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── getMenuItems ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Returns menu items for the given restaurant")
    void should_ReturnMenuItems_When_RestaurantHasItems() {
        // Arrange
        given(menuItemRepository.findByRestaurantId(1L)).willReturn(List.of(
                buildMenuItem(10L, "Cheeseburger", 1L),
                buildMenuItem(11L, "Fries", 1L)
        ));

        // Act
        final var result = restaurantService.getMenuItems(1L);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting("name")
                .containsExactlyInAnyOrder("Cheeseburger", "Fries");
    }

    @Test
    @DisplayName("Returns empty list when restaurant has no menu items")
    void should_ReturnEmptyList_When_RestaurantHasNoMenuItems() {
        // Arrange
        given(menuItemRepository.findByRestaurantId(1L)).willReturn(List.of());

        // Act & Assert
        assertThat(restaurantService.getMenuItems(1L)).isEmpty();
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Restaurant buildRestaurant(Long id, String name, boolean isOpen) {
        final var r = new Restaurant();
        r.setId(id);
        r.setName(name);
        r.setCuisine("American");
        r.setAddress("123 Main St");
        r.setRating(4.5);
        r.setIsOpen(isOpen);
        return r;
    }

    private MenuItem buildMenuItem(Long id, String name, Long restaurantId) {
        final var item = new MenuItem();
        item.setId(id);
        item.setName(name);
        item.setPrice(new BigDecimal("9.99"));
        item.setCategory("Mains");
        item.setRestaurantId(restaurantId);
        item.setIsAvailable(true);
        return item;
    }
}
