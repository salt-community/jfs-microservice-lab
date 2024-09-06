package se.saltcode.service;

import org.springframework.stereotype.Service;
import se.saltcode.order.model.Order;
import se.saltcode.order.model.OrderResponseObject;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    public OrderService() {
    }

    public List<Order> getOrders() {
        return null;
    }

    public Order getOrder(UUID id) {
    }

    public UUID createOrder(Order order) {
    }

    public void deleteOrder(UUID id) {
    }
}