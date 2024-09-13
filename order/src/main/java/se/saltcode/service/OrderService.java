package se.saltcode.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.components.MessageRelay;
import se.saltcode.error.InsufficientInventoryException;
import se.saltcode.error.NoSuchOrderException;
import se.saltcode.model.enums.Event;
import se.saltcode.model.order.Orders;
import se.saltcode.model.transaction.Transaction;
import se.saltcode.repository.IOrderRepository;
import se.saltcode.repository.TransactionDbRepository;
import java.util.*;

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

    public void deleteOrder(UUID id) {

        if(orderRepository.existsById(id)){
            throw new NoSuchOrderException();
        }

        orderRepository.deleteById(id);
    }

    @Transactional
    public UUID createOrder(Orders order) {

        int inventory = getInventory(order.getInventoryId())-order.getQuantity();
        if (inventory < 0) {
            throw new InsufficientInventoryException();
        }

        HashMap<String, String> multiValueMap = new HashMap<>();
        multiValueMap.put("id", order.getInventoryId().toString());
        multiValueMap.put("change", String.valueOf(order.getQuantity()));

        UUID orderId = orderRepository.save(order).getId();
        transactionRepository.save(new Transaction(Event.PURCHASE,multiValueMap));

        messageRelay.sendUnfinishedMessages();
        return orderId;
    }

    @Transactional
    public Orders updateOrder(Orders order) {

        Orders oldOrder = orderRepository
                .findById(order.getId())
                .orElseThrow(NoSuchOrderException::new);

        int change = order.getQuantity() - oldOrder.getQuantity();

        if (getInventory(order.getInventoryId())-change < 0) {
            throw new InsufficientInventoryException();
        }

        orderRepository.save(order);

        HashMap<String, String> multiValueMap = new HashMap<>();
        multiValueMap.put("id", order.getInventoryId().toString());
        multiValueMap.put("change", String.valueOf(change));
        transactionRepository.save(new Transaction(Event.CHANGE, multiValueMap));

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