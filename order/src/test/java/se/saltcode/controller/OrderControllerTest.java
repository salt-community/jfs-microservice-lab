package se.saltcode.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import se.saltcode.model.order.AddOrderDto;
import se.saltcode.model.order.Order;
import se.saltcode.model.order.OrderDto;
import se.saltcode.service.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldReturnOrders_whenGetOrders() throws Exception {
        when(orderService.getOrders()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"));
    }

    @Test
    void shouldReturnOrder_whenGetOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderDto orderDto = new OrderDto(orderId, productId, 10, 99.99);
        when(orderService.getOrder(orderId)).thenReturn(new Order(orderDto));

        mockMvc.perform(get("/api/order/" + orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderId.toString()));
    }

    @Test
    void shouldCreateOrder_whenPostOrder() throws Exception {
        UUID productId = UUID.randomUUID();
        AddOrderDto addOrderDto = new AddOrderDto(productId, 10, 99.99);
        OrderDto orderDto = new OrderDto(UUID.randomUUID(), productId, 10, 99.99);
        when(orderService.createOrder(any(Order.class))).thenReturn(new Order(orderDto));

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addOrderDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderDto.id().toString()));
    }

    @Test
    void shouldDeleteOrder_whenDeleteOrder() throws Exception {
        UUID orderId = UUID.randomUUID();

        mockMvc.perform(delete("/api/order/" + orderId))
                .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(orderId);
    }

    @Test
    void shouldUpdateOrder_whenPutOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        OrderDto orderDto = new OrderDto(orderId, productId, 10, 199.99);
        when(orderService.updateOrder(any(Order.class))).thenReturn(new Order(orderDto));

        mockMvc.perform(put("/api/order/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderDto.id().toString()));
    }
}
