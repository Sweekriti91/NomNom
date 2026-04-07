package com.nomnom.restaurant.service;

import com.nomnom.restaurant.dto.*;
import com.nomnom.restaurant.model.MenuItem;
import com.nomnom.restaurant.model.Restaurant;
import com.nomnom.restaurant.repository.MenuItemRepository;
import com.nomnom.restaurant.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        var restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setCuisine(request.getCuisine());
        restaurant.setAddress(request.getAddress());
        restaurant.setRating(0.0);
        restaurant.setIsOpen(true);
        var saved = restaurantRepository.save(restaurant);
        return mapToResponse(saved, List.of());
    }

    public List<RestaurantResponse> getAllRestaurants() {
        return restaurantRepository.findAll().stream()
                .map(r -> mapToResponse(r, menuItemRepository.findByRestaurantId(r.getId())))
                .toList();
    }

    public RestaurantResponse getRestaurantWithMenu(Long id) {
        var restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
        var menuItems = menuItemRepository.findByRestaurantId(id);
        return mapToResponse(restaurant, menuItems);
    }

    public RestaurantResponse toggleOpenClosed(Long id) {
        var restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
        restaurant.setIsOpen(!restaurant.getIsOpen());
        var saved = restaurantRepository.save(restaurant);
        return mapToResponse(saved, menuItemRepository.findByRestaurantId(id));
    }

    public MenuItemResponse addMenuItem(Long restaurantId, CreateMenuItemRequest request) {
        restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + restaurantId));

        var menuItem = new MenuItem();
        menuItem.setName(request.getName());
        menuItem.setDescription(request.getDescription());
        menuItem.setPrice(request.getPrice());
        menuItem.setCategory(request.getCategory());
        menuItem.setRestaurantId(restaurantId);
        menuItem.setIsAvailable(true);
        var saved = menuItemRepository.save(menuItem);
        return mapToMenuItemResponse(saved);
    }

    public List<MenuItemResponse> getMenuItems(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId).stream()
                .map(this::mapToMenuItemResponse)
                .toList();
    }

    private RestaurantResponse mapToResponse(Restaurant restaurant, List<MenuItem> menuItems) {
        var response = new RestaurantResponse();
        response.setId(restaurant.getId());
        response.setName(restaurant.getName());
        response.setCuisine(restaurant.getCuisine());
        response.setAddress(restaurant.getAddress());
        response.setRating(restaurant.getRating());
        response.setIsOpen(restaurant.getIsOpen());
        response.setMenuItems(menuItems.stream().map(this::mapToMenuItemResponse).toList());
        return response;
    }

    private MenuItemResponse mapToMenuItemResponse(MenuItem item) {
        var response = new MenuItemResponse();
        response.setId(item.getId());
        response.setName(item.getName());
        response.setDescription(item.getDescription());
        response.setPrice(item.getPrice());
        response.setCategory(item.getCategory());
        response.setRestaurantId(item.getRestaurantId());
        response.setIsAvailable(item.getIsAvailable());
        return response;
    }
}
