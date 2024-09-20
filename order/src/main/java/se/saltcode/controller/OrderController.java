package se.saltcode.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.model.order.AddOrderDTO;
import se.saltcode.model.order.OrderDTO;
import se.saltcode.model.order.UpdateOrderDTO;
import se.saltcode.model.order.Order;
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
  ResponseEntity<List<OrderDTO>> getOrders() {
    return ResponseEntity.ok(
        orderService.getOrders().stream().map(Order::toResponseObject).toList());
  }

  @GetMapping("/{id}")
  ResponseEntity<OrderDTO> getOrder(@PathVariable UUID id) {
    return ResponseEntity.ok(orderService.getOrder(id).toResponseObject());
  }

  @PostMapping
  ResponseEntity<OrderDTO> createOrder(
      @RequestBody AddOrderDTO addOrderDTO) {
    OrderDTO order =
        orderService.createOrder(new Order(addOrderDTO)).toResponseObject();
    return ResponseEntity.created(URI.create(apiUri + "/" + order.id())).body(order);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping()
  ResponseEntity<OrderDTO> updateOrder(
      @RequestBody UpdateOrderDTO updateOrderDTO) {
    return ResponseEntity.ok(
        orderService.updateOrder(new Order(updateOrderDTO)).toResponseObject());
  }
}
