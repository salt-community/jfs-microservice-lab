package se.saltcode.inventory.controller;

import se.saltcode.inventory.model.Inventory;
import se.saltcode.inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    //very sad to not be able to use field injection anymore :(
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventoryItems() {
        List<Inventory> items = inventoryService.getAllInventoryItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    // Get a single inventory item by ID (UUID)
    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryItemById(@PathVariable("id") UUID id) {
        Inventory item = inventoryService.getInventoryItemById(id);
        if (item != null) {
            return new ResponseEntity<>(item, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventoryItem(@RequestBody Inventory inventory) {
        Inventory createdItem = inventoryService.createInventoryItem(inventory);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventoryItem(@PathVariable("id") UUID id, @RequestBody Inventory inventory) {
        Inventory updatedItem = inventoryService.updateInventoryItem(id, inventory);
        if (updatedItem != null) {
            return new ResponseEntity<>(updatedItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable("id") UUID id) {
        boolean deleted = inventoryService.deleteInventoryItem(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
