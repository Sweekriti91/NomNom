package com.nomnom.order.service;

import com.nomnom.order.dto.CreateOrderRequest;
import com.nomnom.order.dto.OrderResponse;
import com.nomnom.order.model.Order;
import com.nomnom.order.model.OrderStatus;
import com.nomnom.order.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        var order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setRestaurantId(request.getRestaurantId());
        order.setItems(String.join(",", request.getItems()));
        order.setTotalPrice(request.getTotalPrice());
        order.setStatus(OrderStatus.PLACED);

        var saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    public OrderResponse getOrderById(Long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return mapToResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        var order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setStatus(status);
        var saved = orderRepository.save(order);
        return mapToResponse(saved);
    }

    public OrderResponse cancelOrder(Long id) {
        return updateOrderStatus(id, OrderStatus.CANCELLED);
    }

    private OrderResponse mapToResponse(Order order) {
        var response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setRestaurantId(order.getRestaurantId());
        response.setItems(order.getItems());
        response.setStatus(order.getStatus());
        response.setTotalPrice(order.getTotalPrice());
        response.setCreatedAt(order.getCreatedAt());
        response.setUpdatedAt(order.getUpdatedAt());
        return response;
    }
}
