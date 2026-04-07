package com.nomnom.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    private final ServiceConfig serviceConfig;

    public RestClientConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    @Bean
    public RestClient orderServiceClient() {
        return RestClient.builder()
                .baseUrl(serviceConfig.getOrderServiceUrl())
                .build();
    }

    @Bean
    public RestClient restaurantServiceClient() {
        return RestClient.builder()
                .baseUrl(serviceConfig.getRestaurantServiceUrl())
                .build();
    }

    @Bean
    public RestClient notificationServiceClient() {
        return RestClient.builder()
                .baseUrl(serviceConfig.getNotificationServiceUrl())
                .build();
    }
}
