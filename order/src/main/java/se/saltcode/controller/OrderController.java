package se.saltcode.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.model.order.Orders;
import se.saltcode.model.order.OrderCreationObject;
import se.saltcode.model.order.OrderResponseObject;
import se.saltcode.model.order.OrderUpdateObject;
import se.saltcode.service.OrderService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("${api.base-path}${api.controllers.orders}")
public class OrderController {

    private final OrderService orderService;

    @Value("${api.base-path}${api.controllers.orders}/")
    public String API_CONTEXT_ROOT;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
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
        UUID orderId = orderService.createOrder(new Orders(orderCreationObject));
        URI location = URI.create("http://localhost:8080" + API_CONTEXT_ROOT + orderId);
        return ResponseEntity.created(location).body(orderId);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    ResponseEntity<OrderResponseObject> updateOrder(@RequestBody OrderUpdateObject orderUpdateObject) {
        return ResponseEntity
                .ok(orderService
                        .updateOrder(new Orders(orderUpdateObject))
                        .toResponseObject());
    }
}
