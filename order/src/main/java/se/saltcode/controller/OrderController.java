package se.saltcode.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.model.order.AddOrderDto;
import se.saltcode.model.order.Order;
import se.saltcode.model.order.OrderDto;
import se.saltcode.service.OrderService;

@RestController
@CrossOrigin
@RequestMapping("${api.base-path}${api.controllers.orders}")
public class OrderController {

  private final OrderService orderService;
  private final String apiUri;

  public OrderController(
      OrderService orderService,
      @Value("${this.base-uri}${api.base-path}${api.controllers.orders}") String apiUri) {
    this.orderService = orderService;
    this.apiUri = apiUri;
  }

  @GetMapping
  ResponseEntity<List<OrderDto>> getOrders() {
    return ResponseEntity.ok(orderService.getOrders().stream().map(Order::toDto).toList());
  }

  @GetMapping("/{id}")
  ResponseEntity<OrderDto> getOrder(@PathVariable UUID id) {
    return ResponseEntity.ok(orderService.getOrder(id).toDto());
  }

  @PostMapping
  ResponseEntity<OrderDto> createOrder(@RequestBody AddOrderDto addOrderDTO) {
    OrderDto orderDto = orderService.createOrder(new Order(addOrderDTO)).toDto();
    return ResponseEntity.created(URI.create(apiUri + "/" + orderDto.id())).body(orderDto);
  }

  @DeleteMapping("/{id}")
  ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping()
  ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDTO) {
    return ResponseEntity.ok(orderService.updateOrder(new Order(orderDTO)).toDto());
  }
}
