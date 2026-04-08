package com.nomnom.order.service;

import com.nomnom.order.dto.UpdateEtaRequest;
import com.nomnom.order.model.DeliveryStatus;
import com.nomnom.order.repository.DeliveryTrackingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import(DeliveryTrackingService.class)
class DeliveryTrackingServiceTest {

    @Autowired
    private DeliveryTrackingRepository trackingRepository;

    @Autowired
    private DeliveryTrackingService deliveryTrackingService;

    @Test
    void should_CreateTrackingAndReturnMappedResponse_When_UpdateEtaForNewOrder() {
        final var request = new UpdateEtaRequest();
        request.setOrderId(1001L);
        request.setDriverName("Alex");
        request.setDriverPhone("555-1234");
        request.setEtaMinutes(18);
        request.setCurrentLocation("5th Ave");

        final var response = deliveryTrackingService.updateEta(request);

        final var saved = trackingRepository.findByOrderId(1001L).orElseThrow();

        assertEquals(1001L, saved.getOrderId());
        assertEquals(DeliveryStatus.ASSIGNED, saved.getDeliveryStatus());
        assertEquals("Alex", saved.getDriverName());
        assertEquals("555-1234", saved.getDriverPhone());
        assertEquals(18, saved.getEtaMinutes());
        assertEquals("5th Ave", saved.getCurrentLocation());

        assertEquals(saved.getId(), response.getId());
        assertEquals(1001L, response.getOrderId());
        assertEquals("ALEX", response.getDriverName());
        assertEquals("ASSIGNED", response.getDeliveryStatus());
        assertNotNull(response.getEstimatedArrival());
    }
}
