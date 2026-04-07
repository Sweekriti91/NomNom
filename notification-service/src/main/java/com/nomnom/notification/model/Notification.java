package com.nomnom.notification.model;

import java.time.LocalDateTime;

public class Notification {

    private String id;
    private Long orderId;
    private String message;
    private NotificationType type;
    private LocalDateTime timestamp;
    private boolean read;

    public Notification() {
    }

    public Notification(String id, Long orderId, String message, NotificationType type) {
        this.id = id;
        this.orderId = orderId;
        this.message = message;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
