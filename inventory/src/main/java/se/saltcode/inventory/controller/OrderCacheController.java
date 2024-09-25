package se.saltcode.inventory.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
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
  private final String apiUri;

  public OrderCacheController(
      OrderCacheService orderCacheService,
      @Value("${this.base-uri}${api.base-path}${api.controllers.order-cache}") String apiUri) {
    this.orderCacheService = orderCacheService;
    this.apiUri = apiUri;
  }

  @GetMapping
  public ResponseEntity<List<OrderCacheDto>> getOrderCache() {
    return ResponseEntity.ok(
        orderCacheService.getAllOrderCacheItems().stream().map(OrderCache::toDto).toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderCacheDto> getOrderCacheById(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(orderCacheService.getOrderCacheById(id).toDto());
  }

  @PostMapping
  public ResponseEntity<OrderCacheDto> createOrderCacheItem(
      @RequestBody AddOrderCacheDto addOrderCacheDto) {
    OrderCacheDto orderCacheDto = orderCacheService.createOrderCacheItem(new OrderCache(addOrderCacheDto)).toDto();
    return ResponseEntity.created(URI.create(apiUri + "/" + orderCacheDto.id()))
        .body(orderCacheDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrderCache(@PathVariable("id") UUID id) {
    orderCacheService.deleteOrderCacheById(id);
    return ResponseEntity.noContent().build();
  }
}
