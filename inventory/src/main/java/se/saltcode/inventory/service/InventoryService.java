package se.saltcode.inventory.service;

import org.springframework.stereotype.Service;
import se.saltcode.inventory.model.Inventory;
import se.saltcode.inventory.model.InventoryDBRepository;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryDBRepository inventoryDBRepository;

    public InventoryService(InventoryDBRepository inventoryDBRepository) {
        this.inventoryDBRepository = inventoryDBRepository;
    }

    // Get all inventory items
    public List<Inventory> getAllInventoryItems() {
        return inventoryDBRepository.findAll();
    }

    // Get a single inventory item by ID (UUID)
    public Inventory getInventoryItemById(UUID id) {
        return inventoryDBRepository.findById(id).orElse(null);
    }

    // Create a new inventory item
    public Inventory createInventoryItem(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory item cannot be null");
        }
        inventory.setId(UUID.randomUUID()); // Generate a random UUID for the new item
        return inventoryDBRepository.save(inventory);
    }

    // Update an existing inventory item
    public Inventory updateInventoryItem(UUID id, Inventory inventory) {
        return inventoryDBRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setProduct(inventory.getProduct());
                    existingItem.setQuantity(inventory.getQuantity());
                    existingItem.setReservedQuantity(inventory.getReservedQuantity());
                    return inventoryDBRepository.save(existingItem);
                })
                .orElse(null);
    }

    // Delete an inventory item
    public boolean deleteInventoryItem(UUID id) {
        if (inventoryDBRepository.existsById(id)) {
            inventoryDBRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Inventory updateQuantityOfInventory(UUID id, int quantity) {
        return inventoryDBRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setQuantity(quantity);
                    return inventoryDBRepository.save(existingItem);
                })
                .orElse(null);
    }
}