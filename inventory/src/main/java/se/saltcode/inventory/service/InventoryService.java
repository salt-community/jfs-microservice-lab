package se.saltcode.inventory.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import se.saltcode.inventory.exception.DuplicateOrderException;
import se.saltcode.inventory.model.cache.OrderCache;
import se.saltcode.inventory.model.inventory.Inventory;
import se.saltcode.inventory.repository.IOrderCacheRepository;
import se.saltcode.inventory.repository.InventoryDBRepository;

@Service
public class InventoryService {

  private final InventoryDBRepository inventoryDBRepository;
  private final IOrderCacheRepository orderCacheRepository;

  public InventoryService(InventoryDBRepository inventoryDBRepository, IOrderCacheRepository orderCacheRepository) {
    this.inventoryDBRepository = inventoryDBRepository;
      this.orderCacheRepository = orderCacheRepository;
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
    return inventoryDBRepository
        .findById(id)
        .map(
            existingItem -> {
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

  public Inventory updateQuantityOfInventory(UUID id, int quantity, UUID transactionId) {
    if(orderCacheRepository.existsById(transactionId)){
     throw new DuplicateOrderException();
    }
    orderCacheRepository.save(new OrderCache(transactionId));
    Inventory inventory = inventoryDBRepository.findById(id).orElseThrow(NoSuchElementException::new);
    inventory.setQuantity(inventory.getQuantity() - quantity);
    if (inventory.getQuantity() < 0) {
      throw new IllegalArgumentException("Inventory item quantity cannot be negative");
    }
    return inventoryDBRepository.save(inventory);

  }
}
