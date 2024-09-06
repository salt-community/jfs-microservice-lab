package se.saltcode.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.order.model.Order;
import se.saltcode.order.model.OrderCreationObject;
import se.saltcode.order.model.OrderResponseObject;
import se.saltcode.service.OrderService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    ResponseEntity<List<OrderResponseObject>> getOrders() {
        return ResponseEntity.ok(orderService.getOrders()
                .stream()
                .map(Order::toResponseObject)
                .toList());
    }

    @GetMapping("/{id}")
    ResponseEntity<OrderResponseObject> getOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrder(id).toResponseObject());
    }

    @PostMapping
    ResponseEntity<URI> createOrder(@RequestBody OrderCreationObject orderCreationObject) throws URISyntaxException {
        return ResponseEntity
                .created(new URI("/order/" + orderService.createOrder(new Order(orderCreationObject))))
                .build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}