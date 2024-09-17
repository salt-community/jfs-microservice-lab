package se.saltcode.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.model.order.OrderCreationObject;
import se.saltcode.model.order.OrderResponseObject;
import se.saltcode.model.order.OrderUpdateObject;
import se.saltcode.model.order.Orders;
import se.saltcode.service.OrderService;

@RestController
@CrossOrigin
@RequestMapping("${api.base-path}${api.controllers.orders}")
public class OrderController {

  private final OrderService orderService;
  private final String apiUri;

  public OrderController(OrderService orderService, String apiUri) {
    this.orderService = orderService;
    this.apiUri = apiUri;
  }

  @GetMapping
  ResponseEntity<List<OrderResponseObject>> getOrders() {
    return ResponseEntity.ok(
        orderService.getOrders().stream().map(Orders::toResponseObject).toList());
  }

  @GetMapping("/{id}")
  ResponseEntity<OrderResponseObject> getOrder(@PathVariable UUID id) {
    return ResponseEntity.ok(orderService.getOrder(id).toResponseObject());
  }

  @PostMapping
  ResponseEntity<OrderResponseObject> createOrder(
      @RequestBody OrderCreationObject orderCreationObject) {
    OrderResponseObject order =
        orderService.createOrder(new Orders(orderCreationObject)).toResponseObject();
    return ResponseEntity.created(URI.create(apiUri + "/" + order.id())).body(order);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping()
  ResponseEntity<OrderResponseObject> updateOrder(
      @RequestBody OrderUpdateObject orderUpdateObject) {
    return ResponseEntity.ok(
        orderService.updateOrder(new Orders(orderUpdateObject)).toResponseObject());
  }
}
