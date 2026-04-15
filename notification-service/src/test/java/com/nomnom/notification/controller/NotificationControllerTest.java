package com.nomnom.notification.controller;

import com.nomnom.notification.exception.ResourceNotFoundException;
import com.nomnom.notification.model.Notification;
import com.nomnom.notification.model.NotificationType;
import com.nomnom.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    // ── POST /api/notifications ───────────────────────────────────────────────

    @Test
    @DisplayName("Returns 201 with created notification when request is valid")
    void should_Return201_When_ValidNotificationRequest() throws Exception {
        // Arrange
        final var notification = buildNotification("abc-123", 1L, "Order placed", NotificationType.ORDER_PLACED);
        given(notificationService.createNotification(eq(1L), eq("Order placed"), eq(NotificationType.ORDER_PLACED)))
                .willReturn(notification);

        // Act & Assert
        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":1,\"message\":\"Order placed\",\"type\":\"ORDER_PLACED\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("abc-123"))
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.type").value("ORDER_PLACED"))
                .andExpect(jsonPath("$.read").value(false));
    }

    @Test
    @DisplayName("Returns 201 for all valid NotificationType values")
    void should_Return201_When_CreatingNotificationWithCancelledType() throws Exception {
        // Arrange
        final var notification = buildNotification("xyz-456", 2L, "Order cancelled", NotificationType.ORDER_CANCELLED);
        given(notificationService.createNotification(eq(2L), eq("Order cancelled"), eq(NotificationType.ORDER_CANCELLED)))
                .willReturn(notification);

        // Act & Assert
        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":2,\"message\":\"Order cancelled\",\"type\":\"ORDER_CANCELLED\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("ORDER_CANCELLED"));
    }

    // ── GET /api/notifications ────────────────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with all notifications")
    void should_Return200WithAllNotifications_When_GettingAll() throws Exception {
        // Arrange
        given(notificationService.getAllNotifications()).willReturn(List.of(
                buildNotification("id-1", 1L, "Placed", NotificationType.ORDER_PLACED),
                buildNotification("id-2", 2L, "Confirmed", NotificationType.ORDER_CONFIRMED)
        ));

        // Act & Assert
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value("id-1"))
                .andExpect(jsonPath("$[1].id").value("id-2"));
    }

    @Test
    @DisplayName("Returns 200 with empty list when no notifications exist")
    void should_Return200WithEmptyList_When_NoNotificationsExist() throws Exception {
        // Arrange
        given(notificationService.getAllNotifications()).willReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ── GET /api/notifications/order/{orderId} ────────────────────────────────

    @Test
    @DisplayName("Returns 200 with notifications for the given orderId")
    void should_Return200WithNotifications_When_GettingByOrderId() throws Exception {
        // Arrange
        given(notificationService.getNotificationsByOrderId(1L)).willReturn(List.of(
                buildNotification("id-1", 1L, "Placed", NotificationType.ORDER_PLACED),
                buildNotification("id-2", 1L, "Confirmed", NotificationType.ORDER_CONFIRMED)
        ));

        // Act & Assert
        mockMvc.perform(get("/api/notifications/order/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].orderId").value(1))
                .andExpect(jsonPath("$[1].orderId").value(1));
    }

    @Test
    @DisplayName("Returns 200 with empty list when no notifications exist for orderId")
    void should_Return200WithEmptyList_When_NoNotificationsForOrderId() throws Exception {
        // Arrange
        given(notificationService.getNotificationsByOrderId(99L)).willReturn(List.of());

        // Act & Assert
        mockMvc.perform(get("/api/notifications/order/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // ── PUT /api/notifications/{id}/read ──────────────────────────────────────

    @Test
    @DisplayName("Returns 200 with notification marked as read")
    void should_Return200_When_MarkingNotificationAsRead() throws Exception {
        // Arrange
        final var notification = buildNotification("abc-123", 1L, "Order placed", NotificationType.ORDER_PLACED);
        notification.setRead(true);
        given(notificationService.markAsRead("abc-123")).willReturn(notification);

        // Act & Assert
        mockMvc.perform(put("/api/notifications/abc-123/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc-123"))
                .andExpect(jsonPath("$.read").value(true));
    }

    @Test
    @DisplayName("Returns 404 when marking non-existent notification as read")
    void should_Return404_When_MarkingNonExistentNotificationAsRead() throws Exception {
        // Arrange
        given(notificationService.markAsRead("non-existent-id"))
                .willThrow(new ResourceNotFoundException("Notification not found with id: non-existent-id"));

        // Act & Assert
        mockMvc.perform(put("/api/notifications/non-existent-id/read"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private Notification buildNotification(String id, Long orderId, String message, NotificationType type) {
        final var n = new Notification(id, orderId, message, type);
        n.setTimestamp(LocalDateTime.now());
        return n;
    }
}
