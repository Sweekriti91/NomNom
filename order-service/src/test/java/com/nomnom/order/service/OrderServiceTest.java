package com.nomnom.order.service;

import com.nomnom.order.dto.CreateOrderRequest;
import com.nomnom.order.exception.ResourceNotFoundException;
import com.nomnom.order.model.Order;
import com.nomnom.order.model.OrderStatus;
import com.nomnom.order.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    // ── createOrder ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Creates order with PLACED status when request is valid")
    void should_CreateOrder_When_ValidRequest() {
        // Arrange
        final var request = buildCreateRequest("Alice", 1L, List.of("Burger", "Fries"), new BigDecimal("15.99"));
        given(orderRepository.save(any(Order.class))).willAnswer(inv -> {
            final Order o = inv.getArgument(0);
            o.setId(1L);
            return o;
        });

        // Act
        final var response = orderService.createOrder(request);

        // Assert
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCustomerName()).isEqualTo("Alice");
        assertThat(response.getRestaurantId()).isEqualTo(1L);
        assertThat(response.getStatus()).isEqualTo(OrderStatus.PLACED);
        assertThat(response.getTotalPrice()).isEqualByComparingTo("15.99");
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Joins items list with commas when persisting an order")
    void should_JoinItemsWithComma_When_CreatingOrder() {
        // Arrange
        final var request = buildCreateRequest("Bob", 2L, List.of("Pizza", "Soda", "Salad"), new BigDecimal("22.50"));
        given(orderRepository.save(any(Order.class))).willAnswer(inv -> {
            final Order o = inv.getArgument(0);
            o.setId(2L);
            return o;
        });

        // Act
        final var response = orderService.createOrder(request);

        // Assert
        assertThat(response.getItems()).isEqualTo("Pizza,Soda,Salad");
    }

    // ── getOrderById ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Returns order response when order exists")
    void should_ReturnOrder_When_OrderExists() {
        // Arrange
        final var order = buildOrder(1L, "Alice", OrderStatus.PLACED);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));

        // Act
        final var response = orderService.getOrderById(1L);

        // Assert
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getCustomerName()).isEqualTo("Alice");
        assertThat(response.getStatus()).isEqualTo(OrderStatus.PLACED);
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when order does not exist")
    void should_ThrowResourceNotFoundException_When_OrderDoesNotExist() {
        // Arrange
        given(orderRepository.findById(99L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> orderService.getOrderById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── getAllOrders ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Returns all orders mapped to responses")
    void should_ReturnAllOrders_When_OrdersExist() {
        // Arrange
        given(orderRepository.findAll()).willReturn(List.of(
                buildOrder(1L, "Alice", OrderStatus.PLACED),
                buildOrder(2L, "Bob", OrderStatus.CONFIRMED)
        ));

        // Act
        final var result = orderService.getAllOrders();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting("customerName")
                .containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    @DisplayName("Returns empty list when no orders exist")
    void should_ReturnEmptyList_When_NoOrdersExist() {
        // Arrange
        given(orderRepository.findAll()).willReturn(List.of());

        // Act & Assert
        assertThat(orderService.getAllOrders()).isEmpty();
    }

    // ── getOrdersByStatus ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Returns orders filtered by the given status")
    void should_ReturnFilteredOrders_When_FilteringByStatus() {
        // Arrange
        given(orderRepository.findByStatus(OrderStatus.PLACED))
                .willReturn(List.of(buildOrder(1L, "Alice", OrderStatus.PLACED)));

        // Act
        final var result = orderService.getOrdersByStatus(OrderStatus.PLACED);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.PLACED);
    }

    @Test
    @DisplayName("Returns empty list when no orders match the requested status")
    void should_ReturnEmptyList_When_NoOrdersMatchStatus() {
        // Arrange
        given(orderRepository.findByStatus(OrderStatus.DELIVERED)).willReturn(List.of());

        // Act & Assert
        assertThat(orderService.getOrdersByStatus(OrderStatus.DELIVERED)).isEmpty();
    }

    // ── updateOrderStatus ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Updates order status when order exists")
    void should_UpdateStatus_When_OrderExists() {
        // Arrange
        final var order = buildOrder(1L, "Alice", OrderStatus.PLACED);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        given(orderRepository.save(any(Order.class))).willAnswer(inv -> inv.getArgument(0));

        // Act
        final var response = orderService.updateOrderStatus(1L, OrderStatus.CONFIRMED);

        // Assert
        assertThat(response.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        verify(orderRepository).save(order);
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when updating status for non-existent order")
    void should_ThrowResourceNotFoundException_When_UpdatingStatusForNonExistentOrder() {
        // Arrange
        given(orderRepository.findById(99L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> orderService.updateOrderStatus(99L, OrderStatus.CONFIRMED))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── cancelOrder ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("Cancels order by setting status to CANCELLED")
    void should_CancelOrder_When_OrderExists() {
        // Arrange
        final var order = buildOrder(1L, "Alice", OrderStatus.PLACED);
        given(orderRepository.findById(1L)).willReturn(Optional.of(order));
        given(orderRepository.save(any(Order.class))).willAnswer(inv -> inv.getArgument(0));

        // Act
        final var response = orderService.cancelOrder(1L);

        // Assert
        assertThat(response.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when cancelling non-existent order")
    void should_ThrowResourceNotFoundException_When_CancellingNonExistentOrder() {
        // Arrange
        given(orderRepository.findById(99L)).willReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> orderService.cancelOrder(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Order buildOrder(Long id, String customerName, OrderStatus status) {
        final var order = new Order();
        order.setId(id);
        order.setCustomerName(customerName);
        order.setRestaurantId(1L);
        order.setStatus(status);
        order.setTotalPrice(new BigDecimal("10.00"));
        return order;
    }

    private CreateOrderRequest buildCreateRequest(String customerName, Long restaurantId,
                                                  List<String> items, BigDecimal price) {
        final var request = new CreateOrderRequest();
        request.setCustomerName(customerName);
        request.setRestaurantId(restaurantId);
        request.setItems(items);
        request.setTotalPrice(price);
        return request;
    }
}
