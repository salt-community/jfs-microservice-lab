package se.saltcode.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.saltcode.components.MessageRelay;
import se.saltcode.error.NoSuchOrderException;
import se.saltcode.model.enums.Event;
import se.saltcode.model.order.Orders;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.TransactionDbRepository;
import java.util.*;

@Service
public class OrderService {

    private final IOrderRepository orderRepository;
    private final TransactionDbRepository transactionRepository;
    private final MessageRelay messageRelay;

    public OrderService(IOrderRepository orderRepository,MessageRelay messageRelay, TransactionDbRepository transactionRepository) {
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
        if(orderRepository.existsById(id)){
            throw new NoSuchOrderException();
        }
        orderRepository.deleteById(id);
    }

    @Transactional
    public UUID createOrder(Orders order) {

        UUID orderId = orderRepository.save(order).getId();

        transactionRepository.save(
                new Transaction(Event.PURCHASE,
                        Map.of("id", order.getInventoryId().toString(),
                                "change", String.valueOf(order.getQuantity()))));

        messageRelay.sendUnfinishedMessages();
        return orderId;
    }

    @Transactional
    public Orders updateOrder(Orders order) {

        Orders oldOrder = orderRepository
                .findById(order.getId())
                .orElseThrow(NoSuchOrderException::new);

        int change = order.getQuantity() - oldOrder.getQuantity();

        orderRepository.save(order);

        transactionRepository.save(
                new Transaction(Event.CHANGE,
                        Map.of("id", order.getInventoryId().toString(),
                                "change", String.valueOf(change))));

        messageRelay.sendUnfinishedMessages();
        return order;

    }
}