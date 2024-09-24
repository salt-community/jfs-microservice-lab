package se.saltcode.inventory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.inventory.model.cache.CreateOrderCacheDto;
import se.saltcode.inventory.model.cache.OrderCache;
import se.saltcode.inventory.service.OrderCacheService;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("${api.base-path}${api.controllers.order-cache}")
public class OrderCacheController {

    private final OrderCacheService orderCacheService;

    public OrderCacheController( OrderCacheService orderCacheService) {
        this.orderCacheService = orderCacheService;
    }

    @GetMapping
    public ResponseEntity<List<OrderCache>> getOrderCache() {
        List<OrderCache> items =
                orderCacheService.getAllOrderCacheItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderCache> getOrderCacheById(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(orderCacheService.getOrderCacheById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderCache> createOrderCacheItem(@RequestBody CreateOrderCacheDto createOrderCacheDto) {
        return new ResponseEntity<>(orderCacheService.createOrderCacheItem(new OrderCache(createOrderCacheDto)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderCache(@PathVariable("id") UUID id) {
        orderCacheService.deleteOrderCacheById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
