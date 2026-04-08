package com.nomnom.order.controller;

import com.nomnom.order.dto.DeliveryTrackingResponse;
import com.nomnom.order.service.DeliveryTrackingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryTrackingController.class)
class DeliveryTrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeliveryTrackingService trackingService;

    @Test
    void should_ReturnOkAndResponseBody_When_UpdateEtaEndpointIsCalled() throws Exception {
        final var response = new DeliveryTrackingResponse();
        response.setId(7L);
        response.setOrderId(22L);
        response.setDriverName("SAM");
        response.setDriverPhone("555-1111");
        response.setEtaMinutes(12);
        response.setCurrentLocation("Downtown");
        response.setDeliveryStatus("EN_ROUTE");

        when(trackingService.updateEta(any())).thenReturn(response);

        mockMvc.perform(put("/api/orders/tracking/eta")
                        .contentType("application/json")
                        .content("""
                                {
                                  "orderId": 22,
                                  "driverName": "Sam",
                                  "driverPhone": "555-1111",
                                  "etaMinutes": 12,
                                  "currentLocation": "Downtown"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(22))
                .andExpect(jsonPath("$.driverName").value("SAM"))
                .andExpect(jsonPath("$.deliveryStatus").value("EN_ROUTE"));

        verify(trackingService).updateEta(any());
    }
}
