package se.saltcode.inventory.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.saltcode.inventory.model.enums.UpdateResult;
import se.saltcode.inventory.model.inventory.AddInventoryDto;
import se.saltcode.inventory.model.inventory.Inventory;
import se.saltcode.inventory.model.inventory.InventoryDto;
import se.saltcode.inventory.service.InventoryService;

@RestController
@CrossOrigin
@RequestMapping("${api.base-path}${api.controllers.inventory}")
public class InventoryController {

  private final InventoryService inventoryService;

  public InventoryController(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  @GetMapping
  public ResponseEntity<List<InventoryDto>> getAllInventoryItems() {
    List<InventoryDto> items =
        inventoryService.getAllInventoryItems().stream()
            .map(Inventory::toResponseObject)
            .collect(Collectors.toList());
    return new ResponseEntity<>(items, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<InventoryDto> getInventoryItemById(@PathVariable("id") UUID id) {
    Inventory item = inventoryService.getInventoryItemById(id);
    return new ResponseEntity<>(item.toResponseObject(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<InventoryDto> createInventoryItem(
      @RequestBody AddInventoryDto addInventoryDTO) {
    Inventory createdItem = inventoryService.createInventoryItem(new Inventory(addInventoryDTO));
    return new ResponseEntity<>(createdItem.toResponseObject(), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<InventoryDto> updateInventoryItem(
      @PathVariable("id") UUID id, @RequestBody InventoryDto inventoryDTO) {
    Inventory inventory = new Inventory(inventoryDTO);
    Inventory updatedItem = inventoryService.updateInventoryItem(id, inventory);
    if (updatedItem != null) {
      return new ResponseEntity<>(updatedItem.toResponseObject(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteInventoryItem(@PathVariable("id") UUID id) {
    boolean deleted = inventoryService.deleteInventoryItem(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @GetMapping("/{id}/quantity")
  public ResponseEntity<Integer> getQuantityOfInventory(@PathVariable("id") UUID id) {
    Inventory item = inventoryService.getInventoryItemById(id);
    return new ResponseEntity<>(item.getQuantity(), HttpStatus.OK);
  }

  @PutMapping("/update/quantity")
  public ResponseEntity<UpdateResult> updateQuantityOfInventory(
      @RequestParam UUID id, @RequestParam int change, @RequestParam UUID transactionId) {
    return new ResponseEntity<>(
        inventoryService.updateQuantityOfInventory(id, change, transactionId), HttpStatus.OK);
  }
}
