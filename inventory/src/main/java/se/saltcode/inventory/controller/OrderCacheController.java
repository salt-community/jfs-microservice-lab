package se.saltcode.inventory.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.inventory.model.cache.AddOrderCacheDto;
import se.saltcode.inventory.model.cache.OrderCache;
import se.saltcode.inventory.model.cache.OrderCacheDto;
import se.saltcode.inventory.service.OrderCacheService;

@RestController
@CrossOrigin
@RequestMapping("${api.base-path}${api.controllers.order-cache}")
public class OrderCacheController {

  private final OrderCacheService orderCacheService;

  public OrderCacheController(OrderCacheService orderCacheService) {
    this.orderCacheService = orderCacheService;
  }

  @GetMapping
  public ResponseEntity<List<OrderCacheDto>> getOrderCache() {
    return new ResponseEntity<>(
        orderCacheService.getAllOrderCacheItems().stream().map(OrderCache::toDto).toList(),
        HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderCacheDto> getOrderCacheById(@PathVariable("id") UUID id) {
    return new ResponseEntity<>(orderCacheService.getOrderCacheById(id).toDto(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<OrderCacheDto> createOrderCacheItem(
      @RequestBody AddOrderCacheDto addOrderCacheDto) {
    return new ResponseEntity<>(
        orderCacheService.createOrderCacheItem(new OrderCache(addOrderCacheDto)).toDto(),
        HttpStatus.CREATED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrderCache(@PathVariable("id") UUID id) {
    orderCacheService.deleteOrderCacheById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
