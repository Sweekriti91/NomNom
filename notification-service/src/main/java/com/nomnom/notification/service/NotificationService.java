package com.nomnom.notification.service;

import com.nomnom.notification.model.Notification;
import com.nomnom.notification.model.NotificationType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    private final ConcurrentHashMap<String, Notification> notifications = new ConcurrentHashMap<>();

    public Notification createNotification(Long orderId, String message, NotificationType type) {
        var id = UUID.randomUUID().toString();
        var notification = new Notification(id, orderId, message, type);
        notifications.put(id, notification);
        return notification;
    }

    public List<Notification> getNotificationsByOrderId(Long orderId) {
        return notifications.values().stream()
                .filter(n -> n.getOrderId().equals(orderId))
                .toList();
    }

    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications.values());
    }

    public Notification markAsRead(String id) {
        var notification = notifications.get(id);
        if (notification == null) {
            throw new RuntimeException("Notification not found with id: " + id);
        }
        notification.setRead(true);
        return notification;
    }
}
