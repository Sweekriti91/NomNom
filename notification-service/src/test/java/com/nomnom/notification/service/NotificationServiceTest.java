package com.nomnom.notification.service;

import com.nomnom.notification.exception.ResourceNotFoundException;
import com.nomnom.notification.model.Notification;
import com.nomnom.notification.model.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationServiceTest {

    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        notificationService = new NotificationService();
    }

    // ── createNotification ────────────────────────────────────────────────────

    @Test
    @DisplayName("Creates notification with correct fields and read=false")
    void should_CreateNotification_With_CorrectFieldsAndUnreadState() {
        // Act
        final var notification = notificationService.createNotification(
                1L, "Your order has been placed", NotificationType.ORDER_PLACED);

        // Assert
        assertThat(notification.getId()).isNotNull().isNotBlank();
        assertThat(notification.getOrderId()).isEqualTo(1L);
        assertThat(notification.getMessage()).isEqualTo("Your order has been placed");
        assertThat(notification.getType()).isEqualTo(NotificationType.ORDER_PLACED);
        assertThat(notification.isRead()).isFalse();
        assertThat(notification.getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("Assigns a unique ID to each notification")
    void should_AssignUniqueIds_When_CreatingMultipleNotifications() {
        // Act
        final var n1 = notificationService.createNotification(1L, "msg1", NotificationType.ORDER_PLACED);
        final var n2 = notificationService.createNotification(1L, "msg2", NotificationType.ORDER_CONFIRMED);

        // Assert
        assertThat(n1.getId()).isNotEqualTo(n2.getId());
    }

    // ── getAllNotifications ───────────────────────────────────────────────────

    @Test
    @DisplayName("Returns all created notifications")
    void should_ReturnAllNotifications_When_NotificationsExist() {
        // Arrange
        notificationService.createNotification(1L, "msg1", NotificationType.ORDER_PLACED);
        notificationService.createNotification(2L, "msg2", NotificationType.ORDER_CONFIRMED);

        // Act
        final List<Notification> all = notificationService.getAllNotifications();

        // Assert
        assertThat(all).hasSize(2);
    }

    @Test
    @DisplayName("Returns empty list when no notifications have been created")
    void should_ReturnEmptyList_When_NoNotificationsExist() {
        // Act & Assert
        assertThat(notificationService.getAllNotifications()).isEmpty();
    }

    // ── getNotificationsByOrderId ─────────────────────────────────────────────

    @Test
    @DisplayName("Returns only notifications matching the given orderId")
    void should_ReturnFilteredNotifications_When_FilteringByOrderId() {
        // Arrange
        notificationService.createNotification(1L, "order 1 placed", NotificationType.ORDER_PLACED);
        notificationService.createNotification(1L, "order 1 confirmed", NotificationType.ORDER_CONFIRMED);
        notificationService.createNotification(2L, "order 2 placed", NotificationType.ORDER_PLACED);

        // Act
        final var result = notificationService.getNotificationsByOrderId(1L);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(n -> n.getOrderId().equals(1L));
    }

    @Test
    @DisplayName("Returns empty list when no notifications exist for the given orderId")
    void should_ReturnEmptyList_When_NoNotificationsForOrderId() {
        // Arrange
        notificationService.createNotification(1L, "msg", NotificationType.ORDER_PLACED);

        // Act
        final var result = notificationService.getNotificationsByOrderId(99L);

        // Assert
        assertThat(result).isEmpty();
    }

    // ── markAsRead ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("Marks notification as read when it exists")
    void should_MarkAsRead_When_NotificationExists() {
        // Arrange
        final var created = notificationService.createNotification(
                1L, "msg", NotificationType.ORDER_PLACED);
        assertThat(created.isRead()).isFalse();

        // Act
        final var updated = notificationService.markAsRead(created.getId());

        // Assert
        assertThat(updated.isRead()).isTrue();
        assertThat(updated.getId()).isEqualTo(created.getId());
    }

    @Test
    @DisplayName("Throws ResourceNotFoundException when marking non-existent notification as read")
    void should_ThrowResourceNotFoundException_When_NotificationDoesNotExist() {
        // Act & Assert
        assertThatThrownBy(() -> notificationService.markAsRead("non-existent-id"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("non-existent-id");
    }

    @Test
    @DisplayName("Mutates the stored notification so subsequent reads reflect the read state")
    void should_PersistReadState_When_MarkedAsRead() {
        // Arrange
        final var created = notificationService.createNotification(
                1L, "msg", NotificationType.ORDER_PLACED);

        // Act
        notificationService.markAsRead(created.getId());

        // Assert — retrieve via getAllNotifications to confirm persistence in map
        final var all = notificationService.getAllNotifications();
        final var found = all.stream().filter(n -> n.getId().equals(created.getId())).findFirst();
        assertThat(found).isPresent();
        assertThat(found.get().isRead()).isTrue();
    }
}
