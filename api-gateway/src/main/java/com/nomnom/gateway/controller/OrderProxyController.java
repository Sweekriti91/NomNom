package com.nomnom.gateway.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/orders")
public class OrderProxyController {

    private final RestClient orderServiceClient;

    public OrderProxyController(@Qualifier("orderServiceClient") RestClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    @GetMapping
    public ResponseEntity<String> getAllOrders() {
        String response = orderServiceClient.get()
                .uri("/api/orders")
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getOrderById(@PathVariable Long id) {
        String response = orderServiceClient.get()
                .uri("/api/orders/{id}", id)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<String> getOrdersByStatus(@PathVariable String status) {
        String response = orderServiceClient.get()
                .uri("/api/orders/status/{status}", status)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody String body) {
        String response = orderServiceClient.post()
                .uri("/api/orders")
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        String response = orderServiceClient.put()
                .uri("/api/orders/{id}/status?status={status}", id, status)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        String response = orderServiceClient.put()
                .uri("/api/orders/{id}/cancel", id)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }
}
