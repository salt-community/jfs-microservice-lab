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
@RequestMapping("/api/order-cache")
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
        OrderCache item = orderCacheService.getOrderCacheById(id);
        if (item != null) {
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<OrderCache> createOrderCacheItem(@RequestBody CreateOrderCacheDto createOrderCacheDto) {
        OrderCache createdItem = orderCacheService.createOrderCacheItem(new OrderCache(createOrderCacheDto));
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderCache(@PathVariable("id") UUID id) {
        boolean deleted = orderCacheService.deleteOrderCacheById(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
