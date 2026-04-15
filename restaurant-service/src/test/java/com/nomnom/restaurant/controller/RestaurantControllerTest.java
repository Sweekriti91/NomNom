package com.nomnom.restaurant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomnom.restaurant.dto.CreateMenuItemRequest;
import com.nomnom.restaurant.dto.CreateRestaurantRequest;
import com.nomnom.restaurant.dto.MenuItemResponse;
import com.nomnom.restaurant.dto.RestaurantResponse;
import com.nomnom.restaurant.exception.ResourceNotFoundException;
import com.nomnom.restaurant.service.RestaurantService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    // ── POST /api/restaurants ─────────────────────────────────────────────────

    @Test
    @DisplayName("Returns 201 with created restaurant when request is valid")
    void should_Return201_When_ValidRestaurantRequest() throws Exception {
        // Arrange
        final var request = new CreateRestaurantRequest();
        request.setName("Burger Palace");
        request.setCuisine("American");
        request.setAddress("123 Main St");

        final var response = buildRestaurantResponse(1L, "Burger Palace", true);
        given(restaurantService.createRestaurant(any())).willReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Burger Palace"))
                .andExpect(jsonPath("$.isOpen").value(true));
    }

    @Test
    @DisplayName("Returns 400 when restaurant name is blank")
    void should_Return400_When_RestaurantNameIsBlank() throws Exception {
        final var request = new CreateRestaurantRequest();
        request.setName("");
        request.setCuisine("American");

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns 400 when restaurant name is missing")
    void should_Return400_When_RestaurantNameIsMissing() throws Exception {
        // name is @NotBlank — sending object without it
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cuisine\":\"American\"}"))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/restaurants ──────────────────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with list of all restaurants")
    void should_Return200WithAllRestaurants_When_GettingAllRestaurants() throws Exception {
        // Arrange
        given(restaurantService.getAllRestaurants()).willReturn(List.of(
                buildRestaurantResponse(1L, "Burger Palace", true),
                buildRestaurantResponse(2L, "Sushi World", false)
        ));

        // Act & Assert
        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Burger Palace"))
                .andExpect(jsonPath("$[1].name").value("Sushi World"));
    }

    @Test
    @DisplayName("Returns 200 with empty list when no restaurants exist")
    void should_Return200WithEmptyList_When_NoRestaurantsExist() throws Exception {
        // Arrange
        given(restaurantService.getAllRestaurants()).willReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ── GET /api/restaurants/{id} ─────────────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with restaurant and menu when restaurant exists")
    void should_Return200_When_RestaurantExists() throws Exception {
        // Arrange
        final var response = buildRestaurantResponse(1L, "Burger Palace", true);
        response.setMenuItems(List.of(buildMenuItemResponse(10L, "Cheeseburger", 1L)));
        given(restaurantService.getRestaurantWithMenu(1L)).willReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.menuItems.length()").value(1))
                .andExpect(jsonPath("$.menuItems[0].name").value("Cheeseburger"));
    }

    @Test
    @DisplayName("Returns 404 when restaurant does not exist")
    void should_Return404_When_RestaurantDoesNotExist() throws Exception {
        // Arrange
        given(restaurantService.getRestaurantWithMenu(99L))
                .willThrow(new ResourceNotFoundException("Restaurant not found with id: 99"));

        // Act & Assert
        mockMvc.perform(get("/api/restaurants/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    // ── PUT /api/restaurants/{id}/toggle ──────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with toggled open status")
    void should_Return200_When_TogglingRestaurantStatus() throws Exception {
        // Arrange
        final var response = buildRestaurantResponse(1L, "Burger Palace", false);
        given(restaurantService.toggleOpenClosed(1L)).willReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/restaurants/1/toggle"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isOpen").value(false));
    }

    @Test
    @DisplayName("Returns 404 when toggling non-existent restaurant")
    void should_Return404_When_TogglingNonExistentRestaurant() throws Exception {
        // Arrange
        given(restaurantService.toggleOpenClosed(99L))
                .willThrow(new ResourceNotFoundException("Restaurant not found with id: 99"));

        // Act & Assert
        mockMvc.perform(put("/api/restaurants/99/toggle"))
                .andExpect(status().isNotFound());
    }

    // ── POST /api/restaurants/{id}/menu ───────────────────────────────────────

    @Test
    @DisplayName("Returns 201 with created menu item when request is valid")
    void should_Return201_When_ValidMenuItemRequest() throws Exception {
        // Arrange
        final var request = new CreateMenuItemRequest();
        request.setName("Cheeseburger");
        request.setDescription("A classic burger");
        request.setPrice(new BigDecimal("9.99"));
        request.setCategory("Mains");

        final var response = buildMenuItemResponse(10L, "Cheeseburger", 1L);
        given(restaurantService.addMenuItem(eq(1L), any())).willReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/restaurants/1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Cheeseburger"))
                .andExpect(jsonPath("$.price").value(9.99));
    }

    @Test
    @DisplayName("Returns 400 when menu item name is blank")
    void should_Return400_When_MenuItemNameIsBlank() throws Exception {
        final var request = new CreateMenuItemRequest();
        request.setName("");
        request.setPrice(new BigDecimal("9.99"));

        mockMvc.perform(post("/api/restaurants/1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns 400 when menu item price is null")
    void should_Return400_When_MenuItemPriceIsNull() throws Exception {
        final var request = new CreateMenuItemRequest();
        request.setName("Burger");
        // price intentionally omitted

        mockMvc.perform(post("/api/restaurants/1/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns 404 when adding menu item to non-existent restaurant")
    void should_Return404_When_AddingMenuItemToNonExistentRestaurant() throws Exception {
        // Arrange
        given(restaurantService.addMenuItem(eq(99L), any()))
                .willThrow(new ResourceNotFoundException("Restaurant not found with id: 99"));

        final var request = new CreateMenuItemRequest();
        request.setName("Burger");
        request.setPrice(new BigDecimal("8.00"));

        // Act & Assert
        mockMvc.perform(post("/api/restaurants/99/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ── GET /api/restaurants/{id}/menu ────────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with menu items for the given restaurant")
    void should_Return200WithMenuItems_When_GettingMenuForRestaurant() throws Exception {
        // Arrange
        given(restaurantService.getMenuItems(1L)).willReturn(List.of(
                buildMenuItemResponse(10L, "Cheeseburger", 1L),
                buildMenuItemResponse(11L, "Fries", 1L)
        ));

        // Act & Assert
        mockMvc.perform(get("/api/restaurants/1/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Cheeseburger"))
                .andExpect(jsonPath("$[1].name").value("Fries"));
    }

    @Test
    @DisplayName("Returns 200 with empty list when restaurant has no menu items")
    void should_Return200WithEmptyList_When_RestaurantHasNoMenuItems() throws Exception {
        // Arrange
        given(restaurantService.getMenuItems(1L)).willReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/restaurants/1/menu"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private RestaurantResponse buildRestaurantResponse(Long id, String name, boolean isOpen) {
        final var response = new RestaurantResponse();
        response.setId(id);
        response.setName(name);
        response.setCuisine("American");
        response.setAddress("123 Main St");
        response.setRating(4.5);
        response.setIsOpen(isOpen);
        response.setMenuItems(List.of());
        return response;
    }

    private MenuItemResponse buildMenuItemResponse(Long id, String name, Long restaurantId) {
        final var response = new MenuItemResponse();
        response.setId(id);
        response.setName(name);
        response.setDescription("Delicious");
        response.setPrice(new BigDecimal("9.99"));
        response.setCategory("Mains");
        response.setRestaurantId(restaurantId);
        response.setIsAvailable(true);
        return response;
    }
}
