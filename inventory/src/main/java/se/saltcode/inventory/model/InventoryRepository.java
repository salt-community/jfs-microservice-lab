package se.saltcode.inventory.model;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InventoryRepository {

    private final InventoryDBRepository inventoryDBRepository;

    // Constructor
    public InventoryRepository(InventoryDBRepository inventoryDBRepository) {
        this.inventoryDBRepository = inventoryDBRepository;
    }

    // Get all inventory items
    public List<Inventory> getAllInventoryItems() {
        return inventoryDBRepository.findAll();
    }

    // Get a single inventory item by ID (UUID)
    public Inventory getInventoryItemById(UUID id) {
        Optional<Inventory> item = inventoryDBRepository.findById(id);
        return item.orElse(null);
    }

    // Save (create) a new inventory item
    public Inventory saveInventoryItem(Inventory inventory) {
        return inventoryDBRepository.save(inventory);  // Save to database
    }

    // Update an existing inventory item
    public Inventory updateInventoryItem(UUID id, Inventory inventory) {
        Optional<Inventory> existingItem = inventoryDBRepository.findById(id);
        if (existingItem.isPresent()) {
            Inventory updatedItem = existingItem.get();
            updatedItem.setProduct(inventory.getProduct());
            updatedItem.setQuantity(inventory.getQuantity());
            updatedItem.setReservedQuantity(inventory.getReservedQuantity());
            return inventoryDBRepository.save(updatedItem);  // Save the updated item
        }
        return null;
    }

    // Delete an inventory item
    public boolean deleteInventoryItem(UUID id) {
        if (inventoryDBRepository.existsById(id)) {
            inventoryDBRepository.deleteById(id);
            return true;
        }
        return false;
    }
}