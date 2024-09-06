package se.saltcode.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.order.model.Order;
import se.saltcode.repository.OrderRepository;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final WebClient webClient;
    private final OrderRepository orderRepository;

    public OrderService(WebClient webClient, OrderRepository orderRepository) {
        this.webClient = webClient;
        this.orderRepository = orderRepository;
    }

    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public Order getOrder(UUID id) {
        return orderRepository.getOrder(id);
    }

    public UUID createOrder(Order order) {
        return orderRepository.createOrder(order);
    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteOrder(id);
    }

    private int getInventory(UUID id){

       return 0;
    }
}