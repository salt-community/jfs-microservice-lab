package se.saltcode.service;
import java.util.*;
import org.springframework.stereotype.Service;
import se.saltcode.components.MessageRelay;
import se.saltcode.exception.NoSuchOrderException;
import se.saltcode.model.enums.Event;
import se.saltcode.model.order.Order;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.ITransactionRepository;

@Service
public class OrderService {

  private final IOrderRepository orderRepository;
  private final ITransactionRepository transactionRepository;
  private final MessageRelay messageRelay;

  public OrderService(
      IOrderRepository orderRepository,
      MessageRelay messageRelay,
      ITransactionRepository transactionRepository) {
    this.orderRepository = orderRepository;
    this.transactionRepository = transactionRepository;
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
    transactionRepository.save(
        new Transaction(
            Event.PURCHASE, newOrder.getId(), newOrder.getInventoryId(), newOrder.getQuantity()));
    messageRelay.sendUnfinishedMessages();
    return newOrder;
  }

  public Order updateOrder(Order order) {

    Order oldOrder = orderRepository.findById(order.getId()).orElseThrow(NoSuchOrderException::new);

    int change = order.getQuantity() - oldOrder.getQuantity();

    orderRepository.save(order);
    transactionRepository.save(
        new Transaction(Event.CHANGE, order.getId(), order.getInventoryId(), change));
    messageRelay.sendUnfinishedMessages();
    return order;
  }
}
