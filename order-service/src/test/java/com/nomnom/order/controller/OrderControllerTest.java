package com.nomnom.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nomnom.order.dto.CreateOrderRequest;
import com.nomnom.order.dto.OrderResponse;
import com.nomnom.order.exception.ResourceNotFoundException;
import com.nomnom.order.model.OrderStatus;
import com.nomnom.order.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    // ── POST /api/orders ──────────────────────────────────────────────────────

    @Test
    @DisplayName("Returns 201 with created order when request is valid")
    void should_Return201_When_ValidOrderRequest() throws Exception {
        // Arrange
        final var request = buildCreateRequest("Alice", 1L);
        final var response = buildOrderResponse(1L, "Alice", OrderStatus.PLACED);
        given(orderService.createOrder(any())).willReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Alice"))
                .andExpect(jsonPath("$.status").value("PLACED"));
    }

    @Test
    @DisplayName("Returns 400 when customerName is blank")
    void should_Return400_When_CustomerNameIsBlank() throws Exception {
        final var request = buildCreateRequest("", 1L);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns 400 when restaurantId is null")
    void should_Return400_When_RestaurantIdIsNull() throws Exception {
        final var request = buildCreateRequest("Alice", null);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns 400 when items list is null")
    void should_Return400_When_ItemsIsNull() throws Exception {
        final var request = new CreateOrderRequest();
        request.setCustomerName("Alice");
        request.setRestaurantId(1L);
        request.setTotalPrice(new BigDecimal("10.00"));
        // items intentionally omitted

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Returns 400 when totalPrice is null")
    void should_Return400_When_TotalPriceIsNull() throws Exception {
        final var request = new CreateOrderRequest();
        request.setCustomerName("Alice");
        request.setRestaurantId(1L);
        request.setItems(List.of("Burger"));
        // totalPrice intentionally omitted

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ── GET /api/orders ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with list of all orders")
    void should_Return200WithAllOrders_When_GettingAllOrders() throws Exception {
        // Arrange
        given(orderService.getAllOrders()).willReturn(List.of(
                buildOrderResponse(1L, "Alice", OrderStatus.PLACED),
                buildOrderResponse(2L, "Bob", OrderStatus.CONFIRMED)
        ));

        // Act & Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].customerName").value("Alice"))
                .andExpect(jsonPath("$[1].customerName").value("Bob"));
    }

    @Test
    @DisplayName("Returns 200 with empty list when no orders exist")
    void should_Return200WithEmptyList_When_NoOrdersExist() throws Exception {
        // Arrange
        given(orderService.getAllOrders()).willReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ── GET /api/orders/{id} ──────────────────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with order when order exists")
    void should_Return200_When_OrderExists() throws Exception {
        // Arrange
        given(orderService.getOrderById(1L))
                .willReturn(buildOrderResponse(1L, "Alice", OrderStatus.PLACED));

        // Act & Assert
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Alice"));
    }

    @Test
    @DisplayName("Returns 404 when order does not exist")
    void should_Return404_When_OrderDoesNotExist() throws Exception {
        // Arrange
        given(orderService.getOrderById(99L))
                .willThrow(new ResourceNotFoundException("Order not found with id: 99"));

        // Act & Assert
        mockMvc.perform(get("/api/orders/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    // ── GET /api/orders/status/{status} ───────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with orders filtered by status")
    void should_Return200_When_GettingOrdersByStatus() throws Exception {
        // Arrange
        given(orderService.getOrdersByStatus(OrderStatus.PLACED))
                .willReturn(List.of(buildOrderResponse(1L, "Alice", OrderStatus.PLACED)));

        // Act & Assert
        mockMvc.perform(get("/api/orders/status/PLACED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("PLACED"));
    }

    @Test
    @DisplayName("Returns 400 when status value is not a valid OrderStatus")
    void should_Return400_When_StatusIsInvalid() throws Exception {
        mockMvc.perform(get("/api/orders/status/INVALID_STATUS"))
                .andExpect(status().isBadRequest());
    }

    // ── PUT /api/orders/{id}/status ───────────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with updated order when status is changed")
    void should_Return200_When_UpdatingOrderStatus() throws Exception {
        // Arrange
        given(orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED))
                .willReturn(buildOrderResponse(1L, "Alice", OrderStatus.CONFIRMED));

        // Act & Assert
        mockMvc.perform(put("/api/orders/1/status").param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    @DisplayName("Returns 404 when updating status for non-existent order")
    void should_Return404_When_UpdatingStatusForNonExistentOrder() throws Exception {
        // Arrange
        given(orderService.updateOrderStatus(eq(99L), any()))
                .willThrow(new ResourceNotFoundException("Order not found with id: 99"));

        // Act & Assert
        mockMvc.perform(put("/api/orders/99/status").param("status", "CONFIRMED"))
                .andExpect(status().isNotFound());
    }

    // ── PUT /api/orders/{id}/cancel ───────────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with cancelled order")
    void should_Return200_When_CancellingOrder() throws Exception {
        // Arrange
        given(orderService.cancelOrder(1L))
                .willReturn(buildOrderResponse(1L, "Alice", OrderStatus.CANCELLED));

        // Act & Assert
        mockMvc.perform(put("/api/orders/1/cancel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    @DisplayName("Returns 404 when cancelling non-existent order")
    void should_Return404_When_CancellingNonExistentOrder() throws Exception {
        // Arrange
        given(orderService.cancelOrder(99L))
                .willThrow(new ResourceNotFoundException("Order not found with id: 99"));

        // Act & Assert
        mockMvc.perform(put("/api/orders/99/cancel"))
                .andExpect(status().isNotFound());
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private CreateOrderRequest buildCreateRequest(String customerName, Long restaurantId) {
        final var request = new CreateOrderRequest();
        request.setCustomerName(customerName);
        request.setRestaurantId(restaurantId);
        request.setItems(List.of("Burger", "Fries"));
        request.setTotalPrice(new BigDecimal("15.99"));
        return request;
    }

    private OrderResponse buildOrderResponse(Long id, String customerName, OrderStatus status) {
        final var response = new OrderResponse();
        response.setId(id);
        response.setCustomerName(customerName);
        response.setRestaurantId(1L);
        response.setItems("Burger,Fries");
        response.setStatus(status);
        response.setTotalPrice(new BigDecimal("15.99"));
        return response;
    }
}
