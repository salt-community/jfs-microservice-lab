package se.saltcode.inventory.controller;

import se.saltcode.inventory.dto.InventoryDTO;
import se.saltcode.inventory.model.Inventory;
import se.saltcode.inventory.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventoryItems() {
        List<InventoryDTO> items = inventoryService.getAllInventoryItems().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventoryItemById(@PathVariable("id") UUID id) {
        Inventory item = inventoryService.getInventoryItemById(id);
        if (item != null) {
            return new ResponseEntity<>(convertToDTO(item), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<InventoryDTO> createInventoryItem(@RequestBody InventoryDTO inventoryDTO) {
        Inventory inventory = convertToEntity(inventoryDTO);
        Inventory createdItem = inventoryService.createInventoryItem(inventory);
        return new ResponseEntity<>(convertToDTO(createdItem), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> updateInventoryItem(@PathVariable("id") UUID id, @RequestBody InventoryDTO inventoryDTO) {
        Inventory inventory = convertToEntity(inventoryDTO);
        Inventory updatedItem = inventoryService.updateInventoryItem(id, inventory);
        if (updatedItem != null) {
            return new ResponseEntity<>(convertToDTO(updatedItem), HttpStatus.OK);
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

    @GetMapping("/{id}/quantity")
    public ResponseEntity<Integer> getQuantityOfInventory(@PathVariable("id") UUID id) {
        Inventory item = inventoryService.getInventoryItemById(id);
        if (item != null) {
            return new ResponseEntity<>(item.getQuantity(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/{id}/{quantity}")
    public ResponseEntity<InventoryDTO> updateQuantityOfInventory(@PathVariable("id") UUID id, @PathVariable("quantity") int quantity) {
        Inventory updatedItem = inventoryService.updateQuantityOfInventory(id, quantity);
        return new ResponseEntity<>(convertToDTO(updatedItem), HttpStatus.OK);

    }
    // Helper methods to convert between DTO and Entity
    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setProduct(inventory.getProduct());
        dto.setQuantity(inventory.getQuantity());
        dto.setReservedQuantity(inventory.getReservedQuantity());
        return dto;
    }

    private Inventory convertToEntity(InventoryDTO dto) {
        Inventory inventory = new Inventory();
        inventory.setId(dto.getId());
        inventory.setProduct(dto.getProduct());
        inventory.setQuantity(dto.getQuantity());
        inventory.setReservedQuantity(dto.getReservedQuantity());
        return inventory;
    }
}
