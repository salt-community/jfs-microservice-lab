package se.saltcode.inventory.service;

import se.saltcode.inventory.model.Inventory;
import org.springframework.stereotype.Service;
import se.saltcode.inventory.model.InventoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryService {

    // list to simulate the inventory database
    private final List<Inventory> inventoryList = new ArrayList<>();

    // repo
    InventoryRepository repo;

    // default constructor
    public InventoryService() {
        // todo: this default constructor should not be used. But if removed the tests will fail.
    }

    // constructor with repository injected
    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    public List<Inventory> getAllInventoryItems() {
        return inventoryList;
    }

    public Inventory getInventoryItemById(UUID id) {
        Optional<Inventory> item = inventoryList.stream()
                .filter(inventory -> inventory.getId().equals(id))
                .findFirst();
        return item.orElse(null);
    }

    public Inventory createInventoryItem(Inventory inventory) {
        inventory.setId(UUID.randomUUID()); // Generate a random UUID for the new item
        inventoryList.add(inventory);
        return inventory;
    }

    public Inventory updateInventoryItem(UUID id, Inventory inventory) {
        Optional<Inventory> existingItem = inventoryList.stream()
                .filter(inv -> inv.getId().equals(id))
                .findFirst();

        if (existingItem.isPresent()) {
            Inventory updatedItem = existingItem.get();
            updatedItem.setProduct(inventory.getProduct());
            updatedItem.setQuantity(inventory.getQuantity());
            updatedItem.setReservedQuantity(inventory.getReservedQuantity());
            return updatedItem;
        } else {
            return null;
        }
    }

    public boolean deleteInventoryItem(UUID id) {
        return inventoryList.removeIf(inventory -> inventory.getId().equals(id));
    }
}