package com.nomnom.gateway.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantProxyController {

    private final RestClient restaurantServiceClient;

    public RestaurantProxyController(@Qualifier("restaurantServiceClient") RestClient restaurantServiceClient) {
        this.restaurantServiceClient = restaurantServiceClient;
    }

    @GetMapping
    public ResponseEntity<String> getAllRestaurants() {
        String response = restaurantServiceClient.get()
                .uri("/api/restaurants")
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getRestaurantById(@PathVariable Long id) {
        String response = restaurantServiceClient.get()
                .uri("/api/restaurants/{id}", id)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> createRestaurant(@RequestBody String body) {
        String response = restaurantServiceClient.post()
                .uri("/api/restaurants")
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<String> toggleOpenClosed(@PathVariable Long id) {
        String response = restaurantServiceClient.put()
                .uri("/api/restaurants/{id}/toggle", id)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/menu")
    public ResponseEntity<String> addMenuItem(@PathVariable Long id, @RequestBody String body) {
        String response = restaurantServiceClient.post()
                .uri("/api/restaurants/{id}/menu", id)
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<String> getMenuItems(@PathVariable Long id) {
        String response = restaurantServiceClient.get()
                .uri("/api/restaurants/{id}/menu", id)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }
}
