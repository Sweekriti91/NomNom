package com.nomnom.order.controller;

import com.nomnom.order.dto.DeliveryTrackingResponse;
import com.nomnom.order.dto.UpdateEtaRequest;
import com.nomnom.order.model.DeliveryStatus;
import com.nomnom.order.service.DeliveryTrackingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/tracking")
public class DeliveryTrackingController {

    private final DeliveryTrackingService trackingService;

    public DeliveryTrackingController(DeliveryTrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @PutMapping("/eta")
    public ResponseEntity<DeliveryTrackingResponse> updateEta(@RequestBody UpdateEtaRequest request) {
        return ResponseEntity.ok(trackingService.updateEta(request));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<DeliveryTrackingResponse> getTracking(@PathVariable Long orderId) {
        return ResponseEntity.ok(trackingService.getTrackingByOrderId(orderId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<DeliveryTrackingResponse> updateStatus(@PathVariable Long orderId,
                                                                  @RequestParam DeliveryStatus status) {
        return ResponseEntity.ok(trackingService.updateDeliveryStatus(orderId, status));
    }
}
