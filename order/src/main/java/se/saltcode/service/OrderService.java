package se.saltcode.service;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import se.saltcode.error.InsufficientInventoryException;
import se.saltcode.model.enums.MessageType;
import se.saltcode.model.message.Message;
import se.saltcode.model.order.Orders;
import se.saltcode.repository.IMessageRepository;
import se.saltcode.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService {

    private final WebClient webClient;
    private final OrderRepository orderRepository;
    private final IMessageRepository messageRepository;

    public OrderService(OrderRepository orderRepository, IMessageRepository messageRepository) {
        this.webClient = WebClient.create("http://localhost:5000/api/inventory/");
        this.orderRepository = orderRepository;
        this.messageRepository = messageRepository;
    }

    public List<Orders> getOrders() {
        return orderRepository.getOrders();
    }

    public Orders getOrder(UUID id) {
        return orderRepository.getOrder(id);
    }


    public UUID createOrder(Orders order) {

        int inventory = getInventory(order.getInventoryId())-order.getQuantity();
        if (inventory < 0) {
            throw new InsufficientInventoryException();
        }
        UUID orderId = orderRepository.createOrder(order);
        UUID messageId = messageRepository.save(
                new Message(
                "http://localhost:5000/api/inventory/"+order.getInventoryId()+"/"+inventory,
                null,
                MessageType.POST)).getId();


        HttpStatusCode response = setInventory(order.getInventoryId(), inventory);
        if(response == HttpStatus.OK) {
            messageRepository.deleteById(messageId);
        }

        return orderId;
    }

    public void deleteOrder(UUID id) {
        orderRepository.deleteOrder(id);
    }

    @Transactional
    public Orders updateOrder(Orders order) {

        int newInventory = getInventory(order.getInventoryId())
                +orderRepository.getOrder(order.getId()).getQuantity()
                -order.getQuantity();

        if (newInventory < 0) {
            throw new InsufficientInventoryException();
        }
        Orders newOrder = orderRepository.updateOrder(order);
        UUID messageId = messageRepository.save(
                new Message(
                        "http://localhost:8080/api/inventory/"+order.getInventoryId()+"/"+newInventory,
                        null,
                        MessageType.POST)).getId();

        HttpStatusCode response = setInventory(order.getInventoryId(), newInventory);

        if(response == HttpStatus.OK) {
            messageRepository.deleteById(messageId);
        }
        return newOrder;

    }

    private int getInventory(UUID id){

       return Optional.ofNullable(webClient
                .get()
                .uri(id+"/quantity")
                .retrieve()
                .bodyToMono(Integer.class)
                .block()).orElseThrow(InternalError::new);

    }

    private HttpStatusCode setInventory(UUID id, int quantity){
        
        WebClient.ResponseSpec response = webClient
                .post()
                .uri(id+"/"+quantity)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve();
        HttpStatusCode code=Optional.of(response.toBodilessEntity().block().getStatusCode()).get();
        return code;
      
    }

}