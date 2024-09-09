package se.saltcode.inventory.service;

import org.springframework.stereotype.Service;
import se.saltcode.inventory.model.Inventory;
import se.saltcode.inventory.model.InventoryRepository;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    // Service methods work with entities, not DTOs

    public List<Inventory> getAllInventoryItems() {
        return repo.getAllInventoryItems();
    }

    public Inventory getInventoryItemById(UUID id) {
        return repo.getInventoryItemById(id);
    }

    public Inventory createInventoryItem(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory item cannot be null");
        }
        inventory.setId(UUID.randomUUID()); // Generate a random UUID for the new item
        return repo.saveInventoryItem(inventory);
    }


    public Inventory updateInventoryItem(UUID id, Inventory inventory) {
        return repo.updateInventoryItem(id, inventory);
    }

    public boolean deleteInventoryItem(UUID id) {
        return repo.deleteInventoryItem(id);
    }
}