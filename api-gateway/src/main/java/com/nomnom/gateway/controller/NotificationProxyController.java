package com.nomnom.gateway.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/api/notifications")
public class NotificationProxyController {

    private final RestClient notificationServiceClient;

    public NotificationProxyController(@Qualifier("notificationServiceClient") RestClient notificationServiceClient) {
        this.notificationServiceClient = notificationServiceClient;
    }

    @GetMapping
    public ResponseEntity<String> getAllNotifications() {
        String response = notificationServiceClient.get()
                .uri("/api/notifications")
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<String> getNotificationsByOrderId(@PathVariable Long orderId) {
        String response = notificationServiceClient.get()
                .uri("/api/notifications/order/{orderId}", orderId)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<String> createNotification(@RequestBody String body) {
        String response = notificationServiceClient.post()
                .uri("/api/notifications")
                .header("Content-Type", "application/json")
                .body(body)
                .retrieve()
                .body(String.class);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<String> markAsRead(@PathVariable String id) {
        String response = notificationServiceClient.put()
                .uri("/api/notifications/{id}/read", id)
                .retrieve()
                .body(String.class);
        return ResponseEntity.ok(response);
    }
}
