package se.saltcode.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.components.MessageRelay;
import se.saltcode.error.InsufficientInventoryException;
import se.saltcode.error.NoSuchOrderException;
import se.saltcode.model.enums.Event;
import se.saltcode.model.order.Orders;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.TransactionDbRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final WebClient webClient;
    private final IOrderRepository orderRepository;
    private final TransactionDbRepository transactionRepository;
    private final MessageRelay messageRelay;

    public OrderService(IOrderRepository orderRepository,MessageRelay messageRelay, TransactionDbRepository transactionRepository) {
        this.webClient = WebClient.create("http://localhost:5000/api/inventory/");
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

    public UUID createOrder(Orders order) {
        int inventory = getInventory(order.getInventoryId())-order.getQuantity();
        if (inventory < 0) {
            throw new InsufficientInventoryException();
        }
        UUID orderId = orderRepository.save(order).getId();
        transactionRepository.save(
                new Transaction(
                        Event.PURCHASE,
                        Map.of("id",
                                order.getInventoryId().toString(),
                                "change",
                                String.valueOf(order.getQuantity()))));

        messageRelay.sendUnfinishedMessages();
        return orderId;
    }

    public void deleteOrder(UUID id) {
        if(orderRepository.existsById(id)){
            throw new NoSuchOrderException();
        }
        orderRepository.deleteById(id);
    }

    @Transactional
    public Orders updateOrder(Orders order) {

        if(!orderRepository.existsById(order.getId())){
            throw new NoSuchOrderException();
        }

        int change =order.getQuantity()-orderRepository.findById(order.getId()).orElseThrow(NoSuchOrderException::new).getQuantity();

        if (getInventory(order.getInventoryId())-change < 0) {
            throw new InsufficientInventoryException();
        }

        orderRepository.save(order);
        transactionRepository.save(
                new Transaction(
                        Event.CHANGE,
                        Map.of("id",
                                order.getInventoryId().toString(),
                                "change",
                                String.valueOf(change))));
        messageRelay.sendUnfinishedMessages();
        return order;

    }

    public int getInventory(UUID id){
       return Optional.ofNullable(webClient
                .get()
                .uri(id+"/quantity")
                .retrieve()
                .bodyToMono(Integer.class)
                .block()).orElseThrow(InternalError::new);

    }

}