package com.nomnom.restaurant.controller;

import com.nomnom.restaurant.dto.*;
import com.nomnom.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createRestaurant(request));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantWithMenu(id));
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<RestaurantResponse> toggleOpenClosed(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.toggleOpenClosed(id));
    }

    @PostMapping("/{id}/menu")
    public ResponseEntity<MenuItemResponse> addMenuItem(@PathVariable Long id,
                                                        @Valid @RequestBody CreateMenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.addMenuItem(id, request));
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuItemResponse>> getMenuItems(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getMenuItems(id));
    }
}
