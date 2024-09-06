package se.saltcode.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.order.model.Order;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final WebClient webClient;

    public OrderService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Order> getOrders() {
        return null;
    }

    public Order getOrder(UUID id) {
        return null;
    }

    public UUID createOrder(Order order) {
        return null;
    }

    public void deleteOrder(UUID id) {
    }
    private int getInventory(UUID id){
       return 0;
    }
}