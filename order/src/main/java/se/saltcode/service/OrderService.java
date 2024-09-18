package se.saltcode.service;

import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.saltcode.components.MessageRelay;
import se.saltcode.exception.NoSuchOrderException;
import se.saltcode.model.enums.Event;
import se.saltcode.model.order.Orders;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.TransactionDbRepository;

@Service
public class OrderService {

  private final IOrderRepository orderRepository;
  private final TransactionDbRepository transactionRepository;
  private final MessageRelay messageRelay;

  public OrderService(
      IOrderRepository orderRepository,
      MessageRelay messageRelay,
      TransactionDbRepository transactionRepository) {
    this.orderRepository = orderRepository;
    this.transactionRepository = transactionRepository;
    this.messageRelay = messageRelay;
  }

  public List<Orders> getOrders() {
    return orderRepository.findAll();
  }

  public Orders getOrder(UUID id) {
    return orderRepository.findById(id).orElseThrow(NoSuchOrderException::new);
  }

  public void deleteOrder(UUID id) {
    if (orderRepository.existsById(id)) {
      throw new NoSuchOrderException();
    }
    orderRepository.deleteById(id);
  }

  @Transactional
  public Orders createOrder(Orders order) {

    Orders newOrder = orderRepository.save(order);

    transactionRepository.save(
        new Transaction(
            Event.PURCHASE,
            Map.of(
                "id",
                newOrder.getInventoryId().toString(),
                "change",
                String.valueOf(newOrder.getQuantity()),
                "orderId",
                newOrder.getId().toString())));

    messageRelay.sendUnfinishedMessages();
    return newOrder;
  }

  @Transactional
  public Orders updateOrder(Orders order) {

    Orders oldOrder =
        orderRepository.findById(order.getId()).orElseThrow(NoSuchOrderException::new);

    int change = order.getQuantity() - oldOrder.getQuantity();

    orderRepository.save(order);

    transactionRepository.save(
        new Transaction(
            Event.CHANGE,
            Map.of(
                "id",
                order.getInventoryId().toString(),
                "change",
                String.valueOf(change),
                "orderId",
                order.getId().toString())));

    messageRelay.sendUnfinishedMessages();
    return order;
  }
}
