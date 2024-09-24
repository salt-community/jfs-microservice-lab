package se.saltcode.service;

import java.util.*;
import org.springframework.stereotype.Service;
import se.saltcode.components.MessageRelay;
import se.saltcode.exception.NoSuchOrderException;
import se.saltcode.model.order.Order;
import se.saltcode.repository.IOrderRepository;

@Service
public class OrderService {

  private final IOrderRepository orderRepository;
  private final MessageRelay messageRelay;

  public OrderService(
      IOrderRepository orderRepository,
      MessageRelay messageRelay) {
    this.orderRepository = orderRepository;
    this.messageRelay = messageRelay;
  }

  public List<Order> getOrders() {
    return orderRepository.findAll();
  }

  public Order getOrder(UUID id) {
    return orderRepository.findById(id).orElseThrow(NoSuchOrderException::new);
  }

  public void deleteOrder(UUID id) {
    if (!orderRepository.existsById(id)) {
      throw new NoSuchOrderException();
    }
    orderRepository.deleteById(id);
  }

  public Order createOrder(Order order) {
    Order newOrder = orderRepository.save(order);
    messageRelay.sendUnfinishedMessages();
    return newOrder;
  }

  public Order updateOrder(Order order) {

    Order oldOrder = orderRepository.findById(order.getId()).orElseThrow(NoSuchOrderException::new);
    oldOrder.update(order);
    orderRepository.save(oldOrder);
    messageRelay.sendUnfinishedMessages();

    return order;
  }
}
