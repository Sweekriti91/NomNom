package com.nomnom.order.service;

import com.nomnom.order.dto.DeliveryTrackingResponse;
import com.nomnom.order.dto.UpdateEtaRequest;
import com.nomnom.order.model.DeliveryStatus;
import com.nomnom.order.model.DeliveryTracking;
import com.nomnom.order.repository.DeliveryTrackingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DeliveryTrackingService {

    private final DeliveryTrackingRepository trackingRepository;

    public DeliveryTrackingService(DeliveryTrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    public DeliveryTrackingResponse updateEta(UpdateEtaRequest request) {
        var tracking = trackingRepository.findByOrderId(request.getOrderId())
                .orElseGet(() -> {
                    var newTracking = new DeliveryTracking();
                    newTracking.setOrderId(request.getOrderId());
                    newTracking.setDeliveryStatus(DeliveryStatus.ASSIGNED);
                    return newTracking;
                });

        tracking.setDriverName(request.getDriverName());
        tracking.setDriverPhone(request.getDriverPhone());
        tracking.setEtaMinutes(request.getEtaMinutes());
        tracking.setCurrentLocation(request.getCurrentLocation());

        var saved = trackingRepository.save(tracking);
        return mapToResponse(saved);
    }

    public DeliveryTrackingResponse getTrackingByOrderId(Long orderId) {
        var tracking = trackingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("No tracking info for order: " + orderId));
        return mapToResponse(tracking);
    }

    public DeliveryTrackingResponse updateDeliveryStatus(Long orderId, DeliveryStatus status) {
        var tracking = trackingRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("No tracking info for order: " + orderId));
        tracking.setDeliveryStatus(status);
        var saved = trackingRepository.save(tracking);
        return mapToResponse(saved);
    }

    private DeliveryTrackingResponse mapToResponse(DeliveryTracking tracking) {
        var response = new DeliveryTrackingResponse();
        response.setId(tracking.getId());
        response.setOrderId(tracking.getOrderId());

        // BUG #1: Null pointer — driverName may be null when driver not yet assigned
        response.setDriverName(tracking.getDriverName().toUpperCase());

        response.setDriverPhone(tracking.getDriverPhone());
        response.setEtaMinutes(tracking.getEtaMinutes());
        response.setCurrentLocation(tracking.getCurrentLocation());
        response.setDeliveryStatus(tracking.getDeliveryStatus().name());
        response.setLastUpdated(tracking.getLastUpdated() != null
                ? tracking.getLastUpdated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                : null);

        if (tracking.getEtaMinutes() != null) {
            var arrival = LocalDateTime.now().plusMinutes(tracking.getEtaMinutes());
            response.setEstimatedArrival(arrival.format(DateTimeFormatter.ofPattern("h:mm a")));
        }

        return response;
    }
}
