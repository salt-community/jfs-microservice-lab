package se.saltcode.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.model.message.Message;
import se.saltcode.model.order.Orders;
import se.saltcode.model.order.OrderCreationObject;
import se.saltcode.model.order.OrderResponseObject;
import se.saltcode.model.order.OrderUpdateObject;
import se.saltcode.repository.IMessageRepository;
import se.saltcode.service.OrderService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("${api.base-path}${api.controllers.orders}")
public class OrderController {

    private final OrderService orderService;
    private final IMessageRepository messageRepository;

    @Value("${api.base-path}${api.controllers.orders}/")
    public String API_CONTEXT_ROOT;

    public OrderController(OrderService orderService, IMessageRepository messageRepository) {
        this.orderService = orderService;
        this.messageRepository = messageRepository;
    }

    @GetMapping
    ResponseEntity<List<OrderResponseObject>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders()
                .stream()
                .map(Orders::toResponseObject)
                .toList());
    }

    @GetMapping("/{id}")
    ResponseEntity<OrderResponseObject> getOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrder(id).toResponseObject());
    }

    @PostMapping
    ResponseEntity<UUID> createOrder(@RequestBody OrderCreationObject orderCreationObject){
        return new ResponseEntity<>(orderService.createOrder(new Orders(orderCreationObject)), HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping()
    ResponseEntity<OrderResponseObject> updateOrder(@RequestBody OrderUpdateObject orderUpdateObject) {
        return ResponseEntity
                .ok(orderService
                        .updateOrder(new Orders(orderUpdateObject))
                        .toResponseObject());
    }


}
