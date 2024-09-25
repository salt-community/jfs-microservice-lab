package se.saltcode.inventory.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
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
  private final String apiUri;

  public InventoryController(InventoryService inventoryService,
                             @Value("${this.base-uri}${api.base-path}${api.controllers.inventory}") String apiUri) {
    this.inventoryService = inventoryService;
    this.apiUri = apiUri;
  }

  @GetMapping
  public ResponseEntity<List<InventoryDto>> getAllInventoryItems() {
    return ResponseEntity.ok(inventoryService.getAllInventoryItems().stream().map(Inventory::toDto).toList());
  }

  @GetMapping("/{id}")
  public ResponseEntity<InventoryDto> getInventoryItemById(@PathVariable("id") UUID id) {
    return ResponseEntity.ok(inventoryService.getInventoryItemById(id).toDto());
  }

  @PostMapping
  public ResponseEntity<InventoryDto> createInventoryItem(
      @RequestBody AddInventoryDto addInventoryDTO) {
    InventoryDto inventoryDto = inventoryService.createInventoryItem(new Inventory(addInventoryDTO)).toDto();
    return ResponseEntity.created(URI.create(apiUri + "/" + inventoryDto.id())).body(inventoryDto);
  }

  @PutMapping()
  public ResponseEntity<InventoryDto> updateInventoryItem(@RequestBody InventoryDto inventoryDTO) {
    return ResponseEntity.ok(inventoryService.updateInventoryItem(new Inventory(inventoryDTO)).toDto());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteInventoryItem(@PathVariable("id") UUID id) {
    inventoryService.deleteInventoryItem(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/update/quantity")
  public ResponseEntity<UpdateResult> updateQuantityOfInventory(
      @RequestParam UUID inventoryId, @RequestParam int change, @RequestParam UUID transactionId) {
    return ResponseEntity.ok(inventoryService.updateQuantityOfInventory(inventoryId, change, transactionId));
  }
}
