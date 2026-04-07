package com.nomnom.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "services")
public class ServiceConfig {

    private String orderServiceUrl = "http://localhost:8081";
    private String restaurantServiceUrl = "http://localhost:8082";
    private String notificationServiceUrl = "http://localhost:8083";

    public String getOrderServiceUrl() {
        return orderServiceUrl;
    }

    public void setOrderServiceUrl(String orderServiceUrl) {
        this.orderServiceUrl = orderServiceUrl;
    }

    public String getRestaurantServiceUrl() {
        return restaurantServiceUrl;
    }

    public void setRestaurantServiceUrl(String restaurantServiceUrl) {
        this.restaurantServiceUrl = restaurantServiceUrl;
    }

    public String getNotificationServiceUrl() {
        return notificationServiceUrl;
    }

    public void setNotificationServiceUrl(String notificationServiceUrl) {
        this.notificationServiceUrl = notificationServiceUrl;
    }
}
