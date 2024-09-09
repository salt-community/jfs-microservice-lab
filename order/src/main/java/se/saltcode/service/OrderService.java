package se.saltcode.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.error.InsufficientInventoryException;
import se.saltcode.model.order.Orders;
import se.saltcode.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

  //  private final WebClient webClient;
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
   //     this.webClient = webClient;
        this.orderRepository = orderRepository;
    }

    public List<Orders> getOrders() {
        return orderRepository.getOrders();
    }

    public Orders getOrder(UUID id) {
        return orderRepository.getOrder(id);
    }

    public UUID createOrder(Orders order) {
        int inventory = getInventory(order.getId())-order.getQuantity();
        if (inventory < 0) {
            throw new InsufficientInventoryException();
        }
        setInventory(order.getId(), inventory);
        return orderRepository.createOrder(order);
    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteOrder(id);
    }

    public Orders updateOrder(Orders order) {

        int newInventory = getInventory(order.getId())
                +orderRepository.getOrder(order.getId()).getQuantity()
                -order.getQuantity();

        if (newInventory < 0) {
            throw new InsufficientInventoryException();
        }
        setInventory(order.getId(), newInventory);
        return orderRepository.updateOrder(order);

    }

    private int getInventory(UUID id){
       /* return Optional.ofNullable(webClient
                .get()
                .uri("/api/inventory/" + id)
                .retrieve()
                .bodyToMono(Integer.class)
                .block()).orElseThrow(InternalError::new);

        */
        return 0;
    }
    private void setInventory(UUID id, int quantity){
        /*
            Optional.ofNullable(webClient
                .post()
                .uri("/api/inventory/" + id+"/"+quantity)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block()).orElseThrow(InternalError::new);

         */
    }
}